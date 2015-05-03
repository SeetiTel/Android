package com.yoloswag.alex.seetitel;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alex on 5/2/15.
 */
public class ImageActivity extends Activity {

    private String urlBase = "http://168.235.152.38:8080/api/v1/whistle/";

    private ProgressDialog simpleWaitDialog;
    private ImageView downloadedImg;

    private WebView wv;
    private ProgressDialog dialog;

    private int id;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.web_layout);
        wv = (WebView) findViewById(R.id.web_viewer);

        dialog = new ProgressDialog(this);
        dialog.setMessage("hold up");
        dialog.setCancelable(false);

        //set up image/web view to start zoomed out and be zoom-in able
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        Display display = getWindowManager().getDefaultDisplay();
        int width=display.getWidth();
        String data="<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width="+width+", initial-scale=0.65 \" /></head>";
        data=data+"<body><center><img width=\""+width+"\" src=\""+(getIntent().getExtras().getString("WEB_URL"))+"\" /></center></body></html>";
        wv.loadData(data, "text/html", null);
        wv.getSettings().setBuiltInZoomControls(true);

        id = getIntent().getExtras().getInt("ID");

        populateWebView(urlBase + id);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateWebView(String Url) {

        showDialog();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject whistle) {
                        try {
                            // get that image bruh
                            wv.loadUrl("http://168.235.152.38:8080" + whistle.getString("content"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        hideDialog();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage(), Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(request);

    }

    private void showDialog() {
        if(!dialog.isShowing())
            dialog.show();
    }

    private void hideDialog() {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    //functions to download image, for some reason really annoying
    public void saveImage(View v) {

        myAsyncTask myWebFetch = new myAsyncTask();
        myWebFetch.execute();
    }

    class myAsyncTask extends AsyncTask<Void, Void, Void>    {
        TextView tv;
        public ProgressDialog dialog;
        myAsyncTask() {
            dialog = new ProgressDialog(ImageActivity.this);
            dialog.setMessage("Loading news...");
            dialog.setCancelable(true);
            dialog.setIndeterminate(true);
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Download Complete @External Storage.", Toast.LENGTH_LONG).show();
            dialog.dismiss();

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }
        protected Void doInBackground(Void... arg0) {
            URL url = null;
            InputStream input = null;
            OutputStream output = null;
            try {
                url = new URL(getIntent().getExtras().getString("WEB_URL"));
            } catch(Exception e ) { }
            try {
                input = url.openStream();
            } catch(Exception e) { }
            try {
                //The sdcard directory e.g. '/sdcard' can be used directly, or
                //more safely abstracted with getExternalStorageDirectory()
                File storagePath = Environment.getExternalStorageDirectory();
                try {
                    output = new FileOutputStream(new File(storagePath, "myImage.png"));
                } catch(Exception e) { }
                try {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    try {
                        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
                            output.write(buffer, 0, bytesRead);
                        }
                    } catch(Exception e) { }
                } finally {
                    try {
                        output.close();
                    } catch (Exception e) { }
                }
            } finally {
                try {
                    input.close();
                } catch (Exception e) { }
            }
            return null;
        }
    }
}

package com.yoloswag.alex.seetitel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 5/2/15.
 */
public class TextActivity extends Activity {

    private String urlBase = "http://168.235.152.38:8080/api/v1/whistle/";

    private ProgressDialog dialog;
    private RecyclerView recList;

    private int id;
    private TextView tv;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.text_viewer);

        dialog = new ProgressDialog(this);
        dialog.setMessage("hold up");
        dialog.setCancelable(false);

        tv = (TextView) findViewById(R.id.text_viewer);
        id = getIntent().getExtras().getInt("ID");
//        tv.setText(getIntent().getExtras().getString("FULL_TEXT"));
        populateTextView(urlBase + id);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void populateTextView(String Url) {

        showDialog();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject whistle) {
                        try {
                            // get that text bruh
                               tv.setText(whistle.getString("content"));

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
}

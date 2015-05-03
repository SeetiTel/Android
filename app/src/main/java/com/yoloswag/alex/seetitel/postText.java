package com.yoloswag.alex.seetitel;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alex on 5/3/15.
 */
public class postText extends Activity {

    private String message = "";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.post_text);

        ActionBar ab = getActionBar();
        ab.setTitle("Post Text Whistle");

        ab.setDisplayHomeAsUpEnabled(true);
    }

    public void send(View v) {

        message = ((EditText) findViewById(R.id.editText)).getText().toString();

        if(message.matches(".*\\w.*")) {

            postString("http://168.235.152.38:8080/api/v1/whistle/new");

            ((EditText) findViewById(R.id.editText)).setText("");

            Toast.makeText(v.getContext(), "Message has been posted!",
                    Toast.LENGTH_LONG).show();
        }
    }

public void postString(String url) {
    StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("POST", response.toString());
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            VolleyLog.d("POST", "Error: " + error.getMessage());
            Log.d("POST", "" + error.getMessage() + "," + error.toString());
        }
    }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("type", "0");
            params.put("data", message);

            return params;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("abc", "value");
            return headers;
        }
    };

    AppController.getInstance().addToRequestQueue(sr);
}
}


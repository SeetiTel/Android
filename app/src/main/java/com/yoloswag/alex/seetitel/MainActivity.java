package com.yoloswag.alex.seetitel;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private String urlBase = "http://168.235.152.38:8080/api/v1/" + "whistles";

    private ProgressDialog dialog;
    private RecyclerView recList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_activity_main);

        dialog = new ProgressDialog(this);
        dialog.setMessage("hold up");
        dialog.setCancelable(false);

        recList = (RecyclerView) findViewById(R.id.my_recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        populateWhistles(urlBase);

        (findViewById(R.id.fab_add)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent i = new Intent(view.getContext(), CameraActivity.class);
                view.getContext().startActivity(i);
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
           if (id == R.id.refresh) {
               populateWhistles(urlBase);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void newText(View v) {
        Intent i = new Intent(v.getContext(), postText.class);
        v.getContext().startActivity(i);
    }


    private void populateWhistles(String Url) {

        final List<Whistle> whistles = new ArrayList<Whistle>();

        showDialog();

        JsonArrayRequest request = new JsonArrayRequest(Url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        try {
                            for (int i = 0; i < jsonArray.length(); ++i) {
                                JSONObject whistle = (JSONObject) jsonArray.get(i);
                                //do something with each whistle
                                Whistle wh = new Whistle();
                                if(whistle.getInt("type") == 2) {
                                    wh.title = "Image";
                                    wh.id = whistle.getInt("id");
                                    wh.dataType = "IMAGE";
                                    wh.description = "An image whistle";
                                    wh.icon = R.drawable.noun_image;
                                    wh.timestamp = whistle.getInt("created");

                                } else if (whistle.getInt("type") == 1) {
                                    wh.title = "Audio";
                                    wh.id = whistle.getInt("id");
                                    wh.dataType = "AUDIO";
                                    wh.description = "An audio whistle";
                                    wh.icon = R.drawable.noun_audio;
                                    wh.timestamp = whistle.getInt("created");

                                } else {
                                    wh.title = "Text";
                                    wh.id = whistle.getInt("id");
                                    wh.dataType = "TEXT";
                                    wh.description = whistle.getString("teaser");
                                    wh.icon = R.drawable.noun_text;
                                    wh.timestamp = whistle.getInt("created");

                                }
                                Log.i("MainActivity", "populate whistles " + "Id: " + wh.id + " " +
                                wh.description);
                                if(!(whistles.contains(wh))) {
                                    whistles.add(wh);
                                }
                            }
                            //POPULATES RECYCLER VIEW AFTER THE REQUEST IS DONE!!
                            for(int j = 0; j < 3; j++) {
                                Log.i("MainActivity", "populate whistles " + "Id: " + whistles.get(j).id + " " +
                                        whistles.get(j).dataType);
                            }
                            MyAdapter ca = new MyAdapter(whistles);
                            recList.setAdapter(ca);

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

     private List<Whistle> createList(int size) {

        List<Whistle> result = new ArrayList<Whistle>();
        for (int i=1; i <= size; i++) {
            Whistle ci = new Whistle();
            ci.title = "PonyPony";
            ci.description = "Horse!";
            ci.icon = R.drawable.noun_image;
            ci.fullText = "AY WHATS UP MY FELLOW COMPUTER PROGRAMMERS HOW ARE YOU DOING ON THIS FINE DAY" +
                    "THIS IS JUST SOME TEST TEXT AND I DONT REALLY FEEL LIKE GETTING THE WHOLE LATIN THING" +
                    "BECAUSE I'M LAZY OR SOMETHING HOPEFULLY THIS IS A GOOD ENOUGH LENGTH TO TEST A TEXTVIEW" +
                    " HOLA";
            ci.dataType = "IMAGE";
            ci.imageURL = "http://i.imgur.com/A4wsrH1.jpg";

            result.add(ci);

        }

        return result;
    }
}

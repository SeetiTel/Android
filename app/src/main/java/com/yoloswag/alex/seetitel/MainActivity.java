package com.yoloswag.alex.seetitel;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycle_activity_main);
        RecyclerView recList = (RecyclerView) findViewById(R.id.my_recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        MyAdapter ca = new MyAdapter(createList(30));
        recList.setAdapter(ca);
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        String[] values = new String [] {"Twilight Sparkle", "Rarity", "Weh" };
//        String[] descriptions = new String[] {"A very pretty pone", "A beautiful pone", "A strange sound" };
//        String[] dataTypes = new String[] {"image", "text", "audio"};
//
//        MyArrayAdapter adapter = new MyArrayAdapter(this, values, descriptions, dataTypes);
//        setListAdapter(adapter);
//    }

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
        if (id == R.id.add) {
            Toast.makeText(getApplicationContext(), "adding!", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

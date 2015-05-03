package com.yoloswag.alex.seetitel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Alex on 5/2/15.
 */
public class TextActivity extends Activity {
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.text_viewer);
        TextView tv = (TextView) findViewById(R.id.text_viewer);
        tv.setText(getIntent().getExtras().getString("FULL_TEXT"));
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}

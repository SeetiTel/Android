package com.yoloswag.alex.seetitel;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Alex on 5/3/15.
 */
//public class AudioActivity extends Activity {
//    private ProgressDialog dialog;
//    private int id;
//    private Button btn;
//    private String url_audio;
//    /**
//     * help to toggle between play and pause.
//     */
//    private boolean playPause;
//    private MediaPlayer mediaPlayer;
//    /**
//     * remain false till media is not completed, inside OnCompletionListener make it true.
//     */
//    private boolean intialStage = true;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.audio_view);
//        btn = (Button) findViewById(R.id.button1);
//
//        dialog = new ProgressDialog(this);
//        dialog.setMessage("hold up");
//        dialog.setCancelable(false);
//
//        id = getIntent().getExtras().getInt("ID");
//
//        populateAudio("http://168.235.152.38:8080/api/v1/whistle/" + id);
//
//
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        btn.setOnClickListener(pausePlay);
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    private View.OnClickListener pausePlay = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View v) {
//
//            if (!playPause) {
//                btn.setBackgroundResource(R.drawable.noun_pause);
//                if (intialStage)
//                    new Player()
//                            .execute(url_audio);
//                else {
//                    if (!mediaPlayer.isPlaying())
//                        mediaPlayer.start();
//                }
//                playPause = true;
//            } else {
//                btn.setBackgroundResource(R.drawable.noun_play);
//                if (mediaPlayer.isPlaying())
//                    mediaPlayer.pause();
//                playPause = false;
//            }
//        }
//    };
//    /**
//     * preparing mediaplayer will take sometime to buffer the content so prepare it inside the background thread and starting it on UI thread.
//     * @author piyush
//     *
//     */
//
//    class Player extends AsyncTask<String, Void, Boolean> {
//        private ProgressDialog progress;
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//            // TODO Auto-generated method stub
//            Boolean prepared;
//            try {
//
//                mediaPlayer.setDataSource(params[0]);
//
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        // TODO Auto-generated method stub
//                        intialStage = true;
//                        playPause=false;
//                        btn.setBackgroundResource(R.drawable.noun_play);
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                });
//                mediaPlayer.prepare();
//                prepared = true;
//            } catch (IllegalArgumentException e) {
//                // TODO Auto-generated catch block
//                Log.d("IllegarArgument", e.getMessage());
//                prepared = false;
//                e.printStackTrace();
//            } catch (SecurityException e) {
//                // TODO Auto-generated catch block
//                prepared = false;
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                // TODO Auto-generated catch block
//                prepared = false;
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                prepared = false;
//                e.printStackTrace();
//            }
//            return prepared;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            if (progress.isShowing()) {
//                progress.cancel();
//            }
//            Log.d("Prepared", "//" + result);
//            mediaPlayer.start();
//
//            intialStage = false;
//        }
//
//        public Player() {
//            progress = new ProgressDialog(AudioActivity.this);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            this.progress.setMessage("Buffering...");
//            this.progress.show();
//
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mediaPlayer != null) {
//            mediaPlayer.reset();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
public class AudioActivity extends Activity implements View.OnClickListener, View.OnTouchListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {


    private ProgressDialog dialog;
    private String url_audio;

    private String base = "http://168.235.152.38:8080/api/v1/whistle/";

    private int id;

    private ImageButton buttonPlayPause;
    private SeekBar seekBarProgress;

    private MediaPlayer mediaPlayer;
    private int mediaFileLengthInMilliseconds; // this value contains the song duration in milliseconds. Look at getDuration() method in MediaPlayer class

    private final Handler handler = new Handler();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_view);
        dialog = new ProgressDialog(this);
        dialog.setMessage("hold up");
        dialog.setCancelable(false);

        id = getIntent().getExtras().getInt("ID");

        populateAudio(base + id);

        ActionBar ab = getActionBar();
        ab.setTitle("Audio");
        ab.setSubtitle("An audio whistle");

        ab.setDisplayHomeAsUpEnabled(true);

        initView();
    }

    /** This method initialise all the views in project*/
    private void initView() {
        buttonPlayPause = (ImageButton)findViewById(R.id.ButtonTestPlayPause);
        buttonPlayPause.setOnClickListener(this);

        seekBarProgress = (SeekBar)findViewById(R.id.SeekBarTestPlay);
        seekBarProgress.setMax(99); // It means 100% .0-99
        seekBarProgress.setOnTouchListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    /** Method which updates the SeekBar primary progress by current song playing position*/
    private void primarySeekBarProgressUpdater() {
        seekBarProgress.setProgress((int)(((float)mediaPlayer.getCurrentPosition()/mediaFileLengthInMilliseconds)*100)); // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    primarySeekBarProgressUpdater();
                }
            };
            handler.postDelayed(notification,1000);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ButtonTestPlayPause){
            /** ImageButton onClick event handler. Method which start/pause mediaplayer playing */
            try {
                mediaPlayer.setDataSource(url_audio); // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3 URL to mediaplayer data source
                mediaPlayer.prepare(); // you must call this method after setup the datasource in setDataSource method. After calling prepare() the instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mediaPlayer.getDuration(); // gets the song length in milliseconds from URL

            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
                buttonPlayPause.setImageResource(R.drawable.noun_pause);
            }else {
                mediaPlayer.pause();
                buttonPlayPause.setImageResource(R.drawable.noun_play);
            }

            primarySeekBarProgressUpdater();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getId() == R.id.SeekBarTestPlay){
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
            if(mediaPlayer.isPlaying()){
                SeekBar sb = (SeekBar)v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
        buttonPlayPause.setImageResource(R.drawable.noun_play);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        seekBarProgress.setSecondaryProgress(percent);
    }

    private void populateAudio(String Url) {

        showDialog();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject whistle) {
                        try {
                            url_audio = "http://168.235.152.38:8080" + whistle.getString("content");

                        } catch (Exception e) {
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





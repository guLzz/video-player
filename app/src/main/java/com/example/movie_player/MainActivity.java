package com.example.movie_player;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.VideoView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    ProgressDialog _dialog;

    private Button _setUrlButton;
    private ImageButton _bbutton, _pbutton, _fbutton;
    private VideoView _vView;

    private SeekBar _sBar;

    private EditText _URLtext;

    //variables
    private int _currentTime;
    private ArrayList<String> _playlist;
    private int _queuePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Starting");
        
        _bbutton = findViewById(R.id.BackVid);
        _pbutton = findViewById(R.id.PlayVid);
        _fbutton = findViewById(R.id.FowardVid);

        //test button
        _setUrlButton = findViewById(R.id.button4);

        _vView = findViewById(R.id.videoView);

        _sBar = findViewById(R.id.seekBar);

        _URLtext = findViewById(R.id.URL);

        _currentTime = 0;

        _vView.setMediaController(null);

        //StartPlaylist();

    }

    //sets up an empty playlist
    private void StartPlaylist(){
        _queuePos = 0;
        _playlist = new ArrayList<>();
        _playlist.add("https://img-9gag-fun.9cache.com/photo/aeMO8Zv_460svvp9.webm");
        //_vView.setVideoPath(_playlist.get(_queuePos));
    }

    public void LoadYT(View v){
        String _url = _URLtext.getText().toString();

        Intent _intent = new Intent(getBaseContext(),YTActivity.class);
        _intent.putExtra("EXTRA_VIDEO_ID", _url);
        startActivity(_intent);
    }

    public void LoadVid(View v){
        _dialog = new ProgressDialog(MainActivity.this);
        _dialog.setMessage("Please wait...");
        _dialog.setCanceledOnTouchOutside(false);
        _dialog.show();

        String _url = _URLtext.getText().toString();

        try{
            Uri _vUri = Uri.parse(_url);
            _vView.setVideoURI(_vUri);
            _vView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    //_pbutton.setText("Pause");
                    _pbutton.setImageResource(R.mipmap.icpause_round);
                }
            });
        }
        catch (Exception ex){

        }

        _vView.requestFocus();
        _vView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                _dialog.dismiss();
                mp.setLooping(false);
                _vView.start();
            }
        });
    }

    public void PlayVid(View v){

        if(!_vView.isPlaying()){
            _vView.start();
            _pbutton.setImageResource(R.mipmap.icpause_round);
        }
        else{
            _vView.pause();
            _pbutton.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

}

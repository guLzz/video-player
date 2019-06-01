package com.example.movie_player;

import android.content.Intent;
import android.content.IntentFilter;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private static final String TAG = "MainActivity";


    private ImageButton _bbutton, _pbutton, _fbutton;
    private VideoView _vView;
    private GestureLibrary gestureLibrary;

    //seekbar
    private SeekBar _sBar;
    private Handler _sbHandler;
    private Runnable _sbRunnable;


    private EditText _URLtext;
    private TextView _currentTimeText , _videoDuration;

    //variables
    private int _currentTime;
    private boolean _isSeeking;
    private ArrayList<Uri> _playlist;
    private int _queuePos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Starting");

        _pbutton = findViewById(R.id.PlayVid);

        _bbutton = findViewById(R.id.BackVid);
        _bbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BackVid(v);
            }
        });

        _bbutton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                PreviousVid(v);
                return true;
            }
        });

        _fbutton = findViewById(R.id.FowardVid);
        _fbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FowardVid(v);
            }
        });

        _fbutton.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                NextVid(v);
                return true;
            }
        });
        _currentTimeText = findViewById(R.id.currentTime);
        _videoDuration = findViewById(R.id.videoDuration);

        //test button

        _vView = findViewById(R.id.videoView);

        _URLtext = findViewById(R.id.URL);

        _currentTime = 0;

        _vView.setMediaController(null);


        StartPlaylist();
        SetVideoEndListener();

        GestureOverlayView gestureOverlayView = findViewById(R.id.gestures);
        gestureOverlayView.addOnGesturePerformedListener(this);
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        //if(!gestureLibrary.load()) finish();

    }

    private void PreviousVid(View v) {
        if(_queuePos == 0){
            _vView.seekTo(0);
        }
        else{
            _queuePos--;
            _vView.setVideoURI(_playlist.get(_queuePos));
            _vView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    setupSB();
                    _vView.start();
                    playCycle();
                    _pbutton.setImageResource(R.mipmap.icpause_round);
                }
            });
        }
    }

    private void NextVid(View v) {
        if(_queuePos == _playlist.size() - 1){
            return;
        }
        else{
            _queuePos++;
            _vView.setVideoURI(_playlist.get(_queuePos));
            _vView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    setupSB();
                    _vView.start();
                    playCycle();
                    _pbutton.setImageResource(R.mipmap.icpause_round);
                }
            });
        }
    }

    private void SetVideoEndListener() {
        _vView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(!_playlist.isEmpty() && _queuePos != _playlist.size() - 1) {
                    _queuePos++;
                    mp.reset();
                    _vView.setVideoURI(_playlist.get(_queuePos));
                    _vView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            setupSB();
                            _vView.start();
                            playCycle();
                            _pbutton.setImageResource(R.mipmap.icpause_round);
                        }
                    });
                }
            }
        });
    }

    //needs to be called everytime a new video is loaded
    private void setupSB() {
        _sbHandler = new Handler();

        _sBar = findViewById(R.id.seekBar);
        _sBar.setMax(_vView.getDuration());
        int _maxDurMS = _vView.getDuration();
        String _maxDur = ConvertMs(_maxDurMS);

        _videoDuration.setText(_maxDur);

        _sBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input){
                    _vView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //responsible for seekbar update
    private void playCycle(){
        String _newTime = "";

        _sBar.setProgress(_vView.getCurrentPosition());

        if(_vView.isPlaying())
        {
            _sbRunnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            _sbHandler.postDelayed(_sbRunnable,1000);

            _currentTime = _vView.getCurrentPosition();

            _newTime = ConvertMs(_currentTime);
            _currentTimeText.setText(_newTime);
        }
    }

    //sets up an empty playlist
    private void StartPlaylist(){
        _queuePos = 0;
        _playlist = new ArrayList<>();
        //https://img-9gag-fun.9cache.com/photo/aeMO8Zv_460svvp9.webm
        //https://v.redd.it/z08avb339n801/DASH_1_2_M
    }

    public void LoadYT(View v){
        String _url = _URLtext.getText().toString();

        Intent _intent = new Intent(getBaseContext(),YTActivity.class);
        _intent.putExtra("EXTRA_VIDEO_URL", _url);
        startActivity(_intent);
    }

    public void LoadVid(View v){

        final String _url = _URLtext.getText().toString();

        if(_playlist.isEmpty()) {
            try {
                Uri _vUri = Uri.parse(_url);
                _playlist.add(_vUri);
                _vView.setVideoURI(_vUri);

            } catch (Exception ex) {

            }
            _vView.requestFocus();
            _vView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    setupSB();
                    _vView.start();
                    playCycle();
                    _pbutton.setImageResource(R.mipmap.icpause_round);
                }
            });
        }
        else {
            try{
                Uri _vUri = Uri.parse(_url);
                _playlist.add(_vUri);
            }
            catch (Exception ex){

            }
        }

        _URLtext.setText("");
    }

    public void PlayVid(View v){
        if(_playlist.size() > 0) {
            if (!_vView.isPlaying()) {
                _vView.start();
                playCycle();
                _pbutton.setImageResource(R.mipmap.icpause_round);
            } else {
                _vView.pause();
                _pbutton.setImageResource(R.mipmap.ic_launchernew_round);
            }
        }
    }

    public void FowardVid(View v){
        _currentTime = _vView.getCurrentPosition();

        //10s atm
        _vView.seekTo(_currentTime + 10000);
        _sBar.setProgress(_vView.getCurrentPosition());
    }

    public void BackVid(View v){
        _currentTime = _vView.getCurrentPosition();

        //10s atm
        _vView.seekTo(_currentTime - 10000);
        _sBar.setProgress(_vView.getCurrentPosition());
    }

    private String ConvertMs(int _ms){
        String _result = "";

        _result = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(_ms),
                    TimeUnit.MILLISECONDS.toSeconds(_ms) - TimeUnit.MILLISECONDS.toMinutes(_ms));

        return _result;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture)
    {
        ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

        for(Prediction prediction : predictions)
        {
            if(prediction.score > 1.0)
            {
                switch (prediction.name)
                {
                    case "Forward":
                        FowardVid(overlay);
                        break;
                    case "Backward":
                        BackVid(overlay);
                        break;
                    case "Play":
                        PlayVid(overlay);
                        break;
                }
                break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        _sbHandler.removeCallbacks(_sbRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _vView.seekTo(_currentTime);
        _vView.pause();

    }

    @Override
    protected void onPause() {
        super.onPause();
        _currentTime = _vView.getCurrentPosition();
        _vView.pause();
    }
}

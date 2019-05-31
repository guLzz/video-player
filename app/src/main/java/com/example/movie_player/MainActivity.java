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
import android.widget.TextView;
import android.widget.VideoView;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private Button _setUrlButton;
    private ImageButton _bbutton, _pbutton, _fbutton;
    private VideoView _vView;

    private SeekBar _sBar;

    private EditText _URLtext;
    private TextView _currentTimeText , _videoDuration;

    //variables
    private int _currentTime;
    private boolean _isSeeking;
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
        _currentTimeText = findViewById(R.id.currentTime);
        _videoDuration = findViewById(R.id.videoDuration);

        //test button
        _setUrlButton = findViewById(R.id.button4);

        _vView = findViewById(R.id.videoView);

        _sBar = findViewById(R.id.seekBar);
        _sBar.setMax(_vView.getDuration());

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

        String _url = _URLtext.getText().toString();

        try{
            Uri _vUri = Uri.parse(_url);
            _vView.setVideoURI(_vUri);
            _vView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
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
                mp.setLooping(false);
                //_videoDuration.setText(_vView.getDuration());
                _vView.start();
                _pbutton.setImageResource(R.mipmap.icpause_round);
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
            _pbutton.setImageResource(R.mipmap.ic_launchernew_round);
        }
    }

    public void setBarListener(SeekBar bar){
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int time;

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                _vView.seekTo(time);
                _currentTime = time;
                _isSeeking = true;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    time = progress;
                    _currentTimeText.setText(time);
                }
            }
        });
    }

}

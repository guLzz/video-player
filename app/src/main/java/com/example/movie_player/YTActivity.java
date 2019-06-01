package com.example.movie_player;


import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YTActivity extends YouTubeBaseActivity {

    private YouTubePlayerView _yView;
    YouTubePlayer.OnInitializedListener _ytOnInitListener;
    private String _videoID, _playlistID, _url;

    //0 -> single video, 1 -> playlist
    private int _playerMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yt);

        _yView = findViewById(R.id.YtView);

        _url = getIntent().getStringExtra("EXTRA_VIDEO_URL");
        GetVideoID(_url);

        _ytOnInitListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(_playerMode == 0) {
                    youTubePlayer.loadVideo(_videoID);
                }
                else
                {
                    youTubePlayer.loadPlaylist(_playlistID);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        _yView.initialize(YoutubeConfig.getApiKey(), _ytOnInitListener);
    }

    private void GetVideoID(String url) {
        int _playlistPos = url.indexOf("playlist");

        if(_playlistPos == -1)
        {
            _playerMode = 0;
            int _vIdPos = url.indexOf("v=");
            int _andPos = url.indexOf("&");

            if(_vIdPos == -1){
                _videoID = url;
            }
            else if(_andPos != -1){
                _videoID = url.substring(_vIdPos + 2 ,_andPos);
            }
            else {
                _videoID = url.substring(_vIdPos + 2);
            }
        }
        else{
            _playerMode = 1;
            int _pIdPos = url.indexOf("list=") + 5;

            _playlistID = url.substring(_pIdPos);
        }

    }
}

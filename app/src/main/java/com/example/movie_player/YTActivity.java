package com.example.movie_player;


import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YTActivity extends YouTubeBaseActivity {

    private YouTubePlayerView _yView;
    YouTubePlayer.OnInitializedListener _ytOnInitListener;
    private String _videoID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yt);

        _yView = findViewById(R.id.YtView);
        _videoID = getIntent().getStringExtra("EXTRA_VIDEO_ID");
        _ytOnInitListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(_videoID);
                //_test.setText(_url);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        _yView.initialize(YoutubeConfig.getApiKey(), _ytOnInitListener);
    }
}

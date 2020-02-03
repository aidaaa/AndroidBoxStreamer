package com.example.androidbox.Impl.full;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.androidbox.MainActivity;
import com.example.androidbox.R;
import com.example.androidbox.myInterface.ExoPlayerFullScreen;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class FullScreenActivity extends AppCompatActivity {

    PlayerView player_full_screen;
    TrackSelector trackSelector;
    SimpleExoPlayer simpleExoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        player_full_screen=findViewById(R.id.player_full_screen);
        String url=getIntent().getStringExtra("url");
        String ch=getIntent().getStringExtra("ch");
        fullScreen(url,ch);
    }

    public void fullScreen(String url,String ch) {
        trackSelector=new DefaultTrackSelector();
        simpleExoPlayer= ExoPlayerFactory.newSimpleInstance(FullScreenActivity.this,trackSelector);
        player_full_screen.setPlayer(simpleExoPlayer);

        Uri uri= Uri.parse(url);
        DataSource.Factory dFactory=new DefaultHttpDataSourceFactory(Util.getUserAgent(this,"exoplayer"));
        if (ch.contains("0"))
        {
            HlsMediaSource mediaSource=new HlsMediaSource.Factory(dFactory).createMediaSource(uri);

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
        else
        {
            MediaSource mediaSource=new ProgressiveMediaSource.Factory(dFactory).createMediaSource(uri);

            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onDestroy() {
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        simpleExoPlayer.stop();
        simpleExoPlayer.release();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(FullScreenActivity.this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}

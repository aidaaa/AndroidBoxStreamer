package com.example.androidbox.Impl;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidbox.myInterface.ExoPlayerVolume;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public class ExoPlayerVolumeImpl implements ExoPlayerVolume
{
    @Override
    public void onOffVolume(SimpleExoPlayer simpleExoPlayer, float volume)
    {
        simpleExoPlayer.setVolume(volume);
    }

    @Override
    public void fullScreen(Activity activity, PlayerView playerView) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) playerView.getLayoutParams();
        params.width=params.MATCH_PARENT;
        params.height=params.MATCH_PARENT;
        playerView.setLayoutParams(params);
    }

    @Override
    public void smallScreen(Activity activity,PlayerView playerView) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) playerView.getLayoutParams();
        params.width=params.MATCH_PARENT;
        params.height=params.MATCH_PARENT;
        playerView.setLayoutParams(params);
    }
}

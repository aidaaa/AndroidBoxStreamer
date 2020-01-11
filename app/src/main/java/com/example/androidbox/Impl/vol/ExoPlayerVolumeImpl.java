package com.example.androidbox.Impl.vol;

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
}

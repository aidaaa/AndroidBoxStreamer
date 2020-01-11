package com.example.androidbox.myInterface;

import android.app.Activity;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public interface ExoPlayerVolume
{
    void onOffVolume(SimpleExoPlayer simpleExoPlayer,float volume);
}

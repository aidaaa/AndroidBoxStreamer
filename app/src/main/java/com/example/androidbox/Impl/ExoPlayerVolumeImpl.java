package com.example.androidbox.Impl;

import com.example.androidbox.myInterface.ExoPlayerVolume;
import com.google.android.exoplayer2.SimpleExoPlayer;

public class ExoPlayerVolumeImpl implements ExoPlayerVolume
{
    @Override
    public void onOffVolume(SimpleExoPlayer simpleExoPlayer, float volume)
    {
        simpleExoPlayer.setVolume(volume);
    }
}

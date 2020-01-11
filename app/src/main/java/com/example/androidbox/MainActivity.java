package com.example.androidbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.androidbox.Impl.full.FullScreenActivity;
import com.example.androidbox.Impl.vol.ExoPlayerVolumeImpl;
import com.example.androidbox.adapter.PlayerAdapter;
import com.example.androidbox.datamodel.PlayerDataModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements PlayerAdapter.PlayerShow{

    RecyclerView recyclerView;
    TrackSelector trackSelector;
    float currentVolum;
    List<SimpleExoPlayer> exoPlayer=new ArrayList<>();
    ExoPlayerVolumeImpl exoPlayerVolume=new ExoPlayerVolumeImpl();
    List<PlayerView> playerViews=new ArrayList<>();
    boolean fs=false;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.rv);
        List<PlayerDataModel> playerDataModels=new ArrayList<>();

        for (int i = 0; i < 4; i++)
        {
            PlayerDataModel playerView=new PlayerDataModel();
            playerView.setUrl("http://192.168.10.74:3001/"+String.valueOf(i+1));
            playerDataModels.add(playerView);
        }

        PlayerAdapter adapter=new PlayerAdapter(this,playerDataModels,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPlayer(String url, PlayerView playerView,int pos)
    {
        trackSelector=new DefaultTrackSelector();
        SimpleExoPlayer exoPlayer= ExoPlayerFactory.newSimpleInstance(this,trackSelector);
        playerView.setPlayer(exoPlayer);
        Uri uri=Uri.parse(url);
        DataSource.Factory daFactory=new DefaultHttpDataSourceFactory(Util.getUserAgent(this,"exoplayer"));
        MediaSource mediaSource=new ProgressiveMediaSource.Factory(daFactory).createMediaSource(uri);
        float currentVolum=exoPlayer.getVolume();
        this.currentVolum=currentVolum;
        exoPlayer.setVolume(0f);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);


        this.exoPlayer.add(pos,exoPlayer);
        this.playerViews.add(pos,playerView);
    }

    //volume
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int i=event.getKeyCode();
        System.out.println(String.valueOf(i));

        if (keyCode==19)
        {
            for (int j = 0; j < 4; j++)
            {
                if (j!=0)
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(j),0f);
            }
            float vol=this.exoPlayer.get(0).getVolume();
            if (vol==0f) {
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(0), currentVolum);
            }else{
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(0), 0f);
            }
        }
        else if (keyCode==20)
        {
            for (int j = 0; j < 4; j++)
            {
                if (j!=1)
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(j),0f);
            }
            float vol=this.exoPlayer.get(1).getVolume();
            if (vol==0f) {
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(1), currentVolum);
            }else{
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(1), 0f);
            }
        }
        //left
        else if (keyCode==21)
        {
            for (int j = 0; j < 4; j++)
            {
                if (j!=2)
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(j),0f);
            }
            float vol=this.exoPlayer.get(2).getVolume();
            if (vol==0f) {
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(2), currentVolum);
            }else{
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(2), 0f);
            }
        }
        //right
        else if (keyCode==22)
        {
            for (int j = 0; j < 4; j++)
            {
                if (j!=3)
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(j),0f);
            }
            float vol=this.exoPlayer.get(3).getVolume();
            if (vol==0f) {
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(3), currentVolum);
            }else{
                exoPlayerVolume.onOffVolume(this.exoPlayer.get(3), 0f);
            }
        }
        //ok
        else if (keyCode==23)
        {
            Intent intent=new Intent(MainActivity.this,FullScreenActivity.class);
            startActivity(intent);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        int i=keyCode;
        int r=repeatCount;
        return super.onKeyMultiple(keyCode, repeatCount, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Log.d("Test", "Long press!");
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < 4; i++) {
            exoPlayer.get(i).stop();
            exoPlayer.get(i).release();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        for (int i = 0; i < 4; i++) {
            exoPlayer.get(i).stop();
            exoPlayer.get(i).release();
        }
        super.onPause();
    }
}
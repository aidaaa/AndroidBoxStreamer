package com.example.androidbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.androidbox.Impl.ExoPlayerVolumeImpl;
import com.example.androidbox.adapter.PlayerAdapter;
import com.example.androidbox.datamodel.PlayerDataModel;
import com.example.androidbox.myInterface.ExoPlayerVolume;
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
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL,false));
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
        exoPlayer.setVolume(0f);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

        float currentVolum=exoPlayer.getVolume();
        this.currentVolum=currentVolum;
        this.exoPlayer.add(pos,exoPlayer);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode==19)
        {
            exoPlayerVolume.onOffVolume(this.exoPlayer.get(0),currentVolum);
        }
        else if (keyCode==20)
        {
            exoPlayerVolume.onOffVolume(this.exoPlayer.get(0),0f);
            exoPlayerVolume.onOffVolume(this.exoPlayer.get(1),currentVolum);
        }
        return super.onKeyUp(keyCode, event);
    }
}
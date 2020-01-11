package com.example.androidbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

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
        float currentVolum=exoPlayer.getVolume();
        this.currentVolum=currentVolum;
        exoPlayer.setVolume(0f);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);


        this.exoPlayer.add(pos,exoPlayer);
        this.playerViews.add(pos,playerView);
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

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (fs)
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if(getSupportActionBar() != null){
                getSupportActionBar().show();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) playerViews.get(0).getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            playerViews.get(0).setLayoutParams(params);
            fs=false;
        }
        else
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            if(getSupportActionBar() != null){
                getSupportActionBar().hide();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) playerViews.get(0).getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            playerViews.get(0).setLayoutParams(params);
            fs=true;
        }
        return super.onKeyLongPress(keyCode, event);
    }
}
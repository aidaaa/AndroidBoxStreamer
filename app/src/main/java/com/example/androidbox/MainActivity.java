package com.example.androidbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
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
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.androidbox.Impl.full.FullScreenActivity;
import com.example.androidbox.Impl.vol.ExoPlayerVolumeImpl;
import com.example.androidbox.adapter.PlayerAdapter;
import com.example.androidbox.datamodel.PlayerDataModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
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

        for (int i = 0; i < 5; i++)
        {
            PlayerDataModel playerView=new PlayerDataModel();
            if (i==0)
            {
                playerView.setUrl("http://saatmedia.ir:1850/TST/tv6_160p/playlist.m3u8");
            }
            else if (i==1){
                playerView.setUrl("http://192.168.10.38:1234");
            }
            else
            {
                 playerView.setUrl("http://192.168.10.74:3001/" + String.valueOf(i));
            }
            playerDataModels.add(playerView);
        }

        PlayerAdapter adapter=new PlayerAdapter(this,playerDataModels,this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPlayer(String url, PlayerView playerView,int pos)
    {
        trackSelector=new DefaultTrackSelector();

        ((DefaultTrackSelector) trackSelector).setParameters(
                ((DefaultTrackSelector) trackSelector).getParameters().buildUpon().setMaxVideoSizeSd()
                        .setPreferredTextLanguage("en")
                        .setPreferredAudioLanguage("en").build());

        SimpleExoPlayer exoPlayer= ExoPlayerFactory.newSimpleInstance(this,trackSelector);
        playerView.setPlayer(exoPlayer);
        Uri uri=Uri.parse(url);
        DataSource.Factory daFactory=new DefaultHttpDataSourceFactory(Util.getUserAgent(this,"exoplayer"));

        if (pos==0)
        {
            HlsMediaSource mediaSource=new HlsMediaSource.Factory(daFactory).createMediaSource(uri);
            //MediaSource mediaSource=new ProgressiveMediaSource.Factory(daFactory).createMediaSource(uri);
            float currentVolum=exoPlayer.getVolume();
            this.currentVolum=currentVolum;
            exoPlayer.setVolume(0f);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);


            this.exoPlayer.add(pos,exoPlayer);
            this.playerViews.add(pos,playerView);
        }
        else
        {
            MediaSource mediaSource=new ProgressiveMediaSource.Factory(daFactory).createMediaSource(uri);
            float currentVolum=exoPlayer.getVolume();
            this.currentVolum=currentVolum;
            exoPlayer.setVolume(0f);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

            exoPlayer.addAudioListener(new AudioListener() {
                @Override
                public void onAudioSessionId(int audioSessionId) {

                }

                @Override
                public void onAudioAttributesChanged(AudioAttributes audioAttributes) {

                }

                @Override
                public void onVolumeChanged(float volume) {

                }
            });

            this.exoPlayer.add(pos,exoPlayer);
            this.playerViews.add(pos,playerView);
        }

    }

    public void fullScreen()
    {
        PlayerView pv=playerViews.get(1);
        GridLayoutManager.LayoutParams layoutParams= (GridLayoutManager.LayoutParams) pv.getLayoutParams();
        layoutParams.height =layoutParams.MATCH_PARENT;
        layoutParams.width=layoutParams.MATCH_PARENT;
        pv.setLayoutParams(layoutParams);
    }

    //volume
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int i=event.getKeyCode();
        System.out.println(String.valueOf(i));
        // if ((event.getFlags() & KeyEvent.FLAG_CANCELED_LONG_PRESS)==0)
        if (!fs) {

            //up
            if (keyCode == 19) {
                for (int j = 0; j < 4; j++) {
                    if (j != 0) {
                        PlayerView pv=playerViews.get(j);
                        pv.setBackgroundResource(0);
                        exoPlayerVolume.onOffVolume(this.exoPlayer.get(j), 0f);
                    }
                }
                float vol = this.exoPlayer.get(0).getVolume();
                if (vol == 0f)
                {
                    PlayerView pv=playerViews.get(0);
                    pv.setBackground(ResourcesCompat.getDrawable(MainActivity.this.getResources(),R.drawable.border,null));
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(0), currentVolum);
                }
                else
                    {
                        PlayerView pv=playerViews.get(0);
                        pv.setBackgroundResource(0);
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(0), 0f);
                }
            }
            //down
            else if (keyCode == 20) {
                for (int j = 0; j < 4; j++) {
                    if (j != 1)
                    {
                        PlayerView pv=playerViews.get(j);
                        pv.setBackgroundResource(0);
                        exoPlayerVolume.onOffVolume(this.exoPlayer.get(j), 0f);
                    }

                }
                float vol = this.exoPlayer.get(1).getVolume();
                if (vol == 0f) {
                    PlayerView pv=playerViews.get(1);
                    pv.setBackground(ResourcesCompat.getDrawable(MainActivity.this.getResources(),R.drawable.border,null));
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(1), currentVolum);
                } else {
                    PlayerView pv=playerViews.get(1);
                    pv.setBackgroundResource(0);
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(1), 0f);
                }
            }
            //left
            else if (keyCode == 21) {
                for (int j = 0; j < 4; j++) {
                    if (j != 2)
                    {
                        PlayerView pv=playerViews.get(j);
                        pv.setBackgroundResource(0);
                        exoPlayerVolume.onOffVolume(this.exoPlayer.get(j), 0f);
                    }

                }
                float vol = this.exoPlayer.get(2).getVolume();
                if (vol == 0f) {
                    PlayerView pv=playerViews.get(2);
                    pv.setBackground(ResourcesCompat.getDrawable(MainActivity.this.getResources(),R.drawable.border,null));
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(2), currentVolum);
                } else {
                    PlayerView pv=playerViews.get(2);
                    pv.setBackgroundResource(0);
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(2), 0f);
                }
            }
            //right
            else if (keyCode == 22) {
                for (int j = 0; j < 4; j++) {
                    if (j != 3) {
                        PlayerView pv=playerViews.get(j);
                        pv.setBackgroundResource(0);
                        exoPlayerVolume.onOffVolume(this.exoPlayer.get(j), 0f);
                    }
                }
                float vol = this.exoPlayer.get(3).getVolume();
                if (vol == 0f) {
                    PlayerView pv=playerViews.get(3);
                    pv.setBackground(ResourcesCompat.getDrawable(MainActivity.this.getResources(),R.drawable.border,null));
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(3), currentVolum);
                } else {
                    PlayerView pv=playerViews.get(3);
                    pv.setBackgroundResource(0);
                    exoPlayerVolume.onOffVolume(this.exoPlayer.get(3), 0f);
                }
            }
            //ok
            else if (keyCode == 23) {
                fs=true;
            }
        }

        else if (fs)
        {
            Intent intent = new Intent(MainActivity.this, FullScreenActivity.class);
            if (keyCode == 19)
            {
                //fullScreen();
                intent.putExtra("ch","0");
                intent.putExtra("url","http://saatmedia.ir:1850/TST/tv6_160p/playlist.m3u8");
            }
            else if (keyCode==20)
            {
                intent.putExtra("ch","1");
                intent.putExtra("url","http://192.168.10.38:1234");
            }
            else if (keyCode==21)
            {
                intent.putExtra("ch","2");
                intent.putExtra("url","http://192.168.10.74:3001/3");
            }
            else if (keyCode==22)
            {
                intent.putExtra("ch","3");
                intent.putExtra("url","http://192.168.10.74:3001/4");
            }
            startActivity(intent);
            fs=false;
        }

        return super.onKeyUp(keyCode, event);
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
        for (int i = 0; i < 5; i++) {
            exoPlayer.get(i).stop();
            exoPlayer.get(i).release();
        }
        super.onPause();
    }
}
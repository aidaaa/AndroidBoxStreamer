package com.example.androidbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.UdpDataSource;
import com.google.android.exoplayer2.util.TimestampAdjuster;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.extractor.ts.TsExtractor.MODE_SINGLE_PMT;

public class Main2Activity extends AppCompatActivity {

    PlayerView pv;
    DefaultTrackSelector trackSelector;
    float currentVolum;
    SimpleExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

       // Uri uri= Uri.parse("http://192.168.10.211:3002/12");
        //Uri uri= Uri.parse("http://192.168.10.74:3005/4");
        //Uri uri= Uri.parse("file:///android_asset/twolang.mp4");
        Uri uri= Uri.parse("http://192.168.10.85:8082/two/nickol.mp4");
       // Uri uri=Uri.parse("udp://@192.168.10.184:12345");

        pv=findViewById(R.id.pv);
        trackSelector=new DefaultTrackSelector();

        exoPlayer= ExoPlayerFactory.newSimpleInstance(this,trackSelector);

      /*  DataSource.Factory factory = () -> new UdpDataSource(3000, 100000);
        ExtractorsFactory tsExtractorFactory = () -> new TsExtractor[]{new TsExtractor(MODE_SINGLE_PMT,
                new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory())};
        MediaSource mediaSource = new ExtractorMediaSource(uri, factory, tsExtractorFactory,null,null);*/

        DataSource.Factory dataSourceFactory=new DefaultDataSourceFactory(this, Util.getUserAgent(this,"EXOPlayer"));

        MediaSource mediaSource=new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);

      /*  trackSelector.setParameters(trackSelector.getParameters().buildUpon().setMaxVideoSizeSd()
        .setPreferredAudioLanguage("per")
        .setPreferredTextLanguage("per"));*/

       // UdpDataSource.Factory udpDataSource = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "CastActivity"));
       // MediaSource mediaSource = new ExtractorMediaSource.Factory(udpDataSource).createMediaSource(uri);

        pv.setPlayer(exoPlayer);
        currentVolum=exoPlayer.getVolume();
      //  exoPlayer.setVolume(0f);
        exoPlayer.prepare(mediaSource);
        exoPlayer.setPlayWhenReady(true);

        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                System.out.println(error.getMessage());
            }
        });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        //up
        if (keyCode == 19) {
            trackSelector.setParameters(
                    trackSelector.getParameters().buildUpon().setMaxVideoSizeSd()
                            .setPreferredTextLanguage("fas")
                            .setPreferredAudioLanguage("fas").build());
           // exoPlayer.setVolume(currentVolum);
        }
        //down
        else if (keyCode == 20) {
            trackSelector.setParameters(
                    trackSelector.getParameters().buildUpon().setMaxVideoSizeSd()
                            .setPreferredTextLanguage("eng")
                            .setPreferredAudioLanguage("eng").build());
        //    exoPlayer.setVolume(currentVolum);
        }

        return super.onKeyUp(keyCode, event);
    }
}

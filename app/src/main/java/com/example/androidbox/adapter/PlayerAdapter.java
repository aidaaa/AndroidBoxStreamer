package com.example.androidbox.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbox.R;
import com.example.androidbox.datamodel.PlayerDataModel;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewolder>
{
    List<PlayerDataModel>dataModel;
    Context context;
    PlayerShow playerShow;

    public PlayerAdapter(Context context,List<PlayerDataModel> dataModel,PlayerShow playerShow) {
        this.context=context;
        this.dataModel = dataModel;
        this.playerShow=playerShow;
    }


    @NonNull
    @Override
    public PlayerViewolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_player_item,parent,false);
        return new PlayerViewolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewolder holder, int position)
    {
        holder.onBind(dataModel.get(position).getUrl(),position);
    }

    @Override
    public int getItemCount() {
        return dataModel.size();
    }



    public class PlayerViewolder extends RecyclerView.ViewHolder
    {
        PlayerView playerView;

        public PlayerViewolder(@NonNull View itemView)
        {
            super(itemView);
            playerView=itemView.findViewById(R.id.pv_recycler_item);
        }

        public void onBind(String url,int pos)
        {
            playerShow.setPlayer(url,playerView,pos);
            /*trackSelector=new DefaultTrackSelector();
            exoPlayer= ExoPlayerFactory.newSimpleInstance(context,trackSelector);
            playerView.setPlayer(exoPlayer);
            Uri uri=Uri.parse(url);
            DataSource.Factory daFactory=new DefaultHttpDataSourceFactory(Util.getUserAgent(context,"exoplayer"));

            MediaSource mediaSource=new ProgressiveMediaSource.Factory(daFactory).createMediaSource(uri);

            float currentVolum=exoPlayer.getVolume();
            exoPlayer.setVolume(0f);
           // playerView.setUseController(false);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);*/
        }
    }
    public interface PlayerShow
    {
        void setPlayer(String url,PlayerView pv,int pos);
    }
}

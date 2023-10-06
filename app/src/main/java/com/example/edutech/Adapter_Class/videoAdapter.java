package com.example.edutech.Adapter_Class;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edutech.Model_Activity.Video_model;
import com.example.edutech.R;
import com.example.edutech.databinding.VideoViewBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import java.util.ArrayList;


public class videoAdapter extends RecyclerView.Adapter<videoAdapter.MyViewHolder> {
   private ArrayList<Video_model> video_models;
   Context context;

   private ExoPlayer player;
    // SimpleExoPlayer simpleExoPlayer;
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(ArrayList<Video_model> VideoList){
        this.video_models = VideoList;
        notifyDataSetChanged();

    }


    public videoAdapter(ArrayList<Video_model> video_models, Context context) {
        this.video_models = video_models;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        stopPlaying();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Video_model videoModel=video_models.get(position);

            holder.binding.videoDesk.setText(videoModel.getVideoName());

//
       Uri uri=Uri.parse(videoModel.getVideoUrl());

        player = new ExoPlayer.Builder(context).build();
        DataSource.Factory dataSourceFactory = new DefaultDataSource.Factory(context);
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
        holder.binding.playerView.setPlayer(player);
        player.setMediaSource(mediaSource);
        player.prepare();
        player.setPlayWhenReady(false);
        holder.binding.pbVideoLoader.setVisibility(View.GONE);


    }


    @Override
    public int getItemCount() {
        return video_models.size();
    }

    public void stopPlaying() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        VideoViewBinding binding;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = VideoViewBinding.bind(itemView);

        }


    }

}

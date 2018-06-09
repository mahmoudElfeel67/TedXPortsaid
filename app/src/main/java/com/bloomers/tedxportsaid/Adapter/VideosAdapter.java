package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Dialog.YoutubeFragment;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideosAdapter  extends RecyclerView.Adapter<VideosAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    private ArrayList<YouTubeVideo> videos;

    public VideosAdapter(AppCompatActivity editActivity, List<YouTubeVideo> videos) {
        this.mContext = new WeakReference<>(editActivity);
        this.videos = new ArrayList<>(videos);
    }

    public void setVideos(ArrayList<YouTubeVideo> videos) {
        this.videos = videos;
    }

    @NonNull
    @Override
    public VideosAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext.get()).inflate(R.layout.item_video, parent, false);
        return new VideosAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {

        return videos.size();
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.video_title)
        TextView video_title;

        @BindView(R.id.video_thumb)
        ImageView video_thumb;


        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }



        @OnClick(R.id.card)
        public void onClick() {

            HeavilyUsed.callSaveDialog(mContext.get(), YoutubeFragment.newInstance(videos.get(getAdapterPosition()).getId()),null);

        }

        void bind() {
            video_title.setText(videos.get(getAdapterPosition()).getTitle());
            GlideApp.with(mContext.get()).load(videos.get(getAdapterPosition()).getThumbnailUrl()).into(video_thumb);
        }

    }

}

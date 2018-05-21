package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.R;

import java.lang.ref.WeakReference;
import butterknife.ButterKnife;

public class VideosAdapter  extends RecyclerView.Adapter<VideosAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;

    public VideosAdapter(AppCompatActivity editActivity) {
        this.mContext = new WeakReference<>(editActivity);
    }

    @NonNull
    @Override
    public VideosAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideosAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {

        return 100;
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {



        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }



        public void onClick() {



        }

        void bind() {
        }

    }

}
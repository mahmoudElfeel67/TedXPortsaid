package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.Dialog.SpeakerDescription;
import com.bloomers.tedxportsaid.Model.Speaker;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    private final ArrayList<Speaker> arrayList;

    public SpeakerAdapter(AppCompatActivity editActivity,ArrayList<Speaker> arrayList) {
        this.mContext = new WeakReference<>(editActivity);
        this.arrayList =arrayList;
    }

    @NonNull
    @Override
    public SpeakerAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_speaker_white, parent, false);
        return new SpeakerAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakerAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.speaker_image) ImageView speaker_image;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.speaker_image)
        public void onClick() {
            HeavilyUsed.callSaveDialog(mContext.get(), SpeakerDescription.newInstance(arrayList.get(getAdapterPosition())), null);
        }

        void bind() {
            Speaker speaker = arrayList.get(getAdapterPosition());
            GlideApp.with(mContext.get()).load("https://firebasestorage.googleapis.com"+speaker.getProf_url()).transition(DrawableTransitionOptions.withCrossFade(300)).into(speaker_image);
        }

    }

}
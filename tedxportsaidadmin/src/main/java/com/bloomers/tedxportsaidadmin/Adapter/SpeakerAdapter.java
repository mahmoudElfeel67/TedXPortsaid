package com.bloomers.tedxportsaidadmin.Adapter;


import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloomers.tedxportsaidadmin.AppController;
import com.bloomers.tedxportsaidadmin.R;
import com.bloomers.tedxportsaidadmin.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaidadmin.Utitltes.other.HeavilyUsed;
import com.bloomers.tedxportsaidadmin.fragments.QuestionsFragment;
import com.bloomers.tedxportsaidadmin.model.Speaker;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SingleItemRowHolder> {

    private final WeakReference<FragmentActivity> mContext;
    private final ArrayList<Speaker> arrayList;

    public SpeakerAdapter(FragmentActivity editActivity, ArrayList arrayList) {
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



        @BindView(R.id.speaker_image)
        ImageView speaker_image;

        @BindView(R.id.speaker_title)
        TextView speaker_session_name;

        @BindView(R.id.shadow)
                View shadow;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.card)
        public void onClick() {

            AppController.getInstance().FragmentEase(mContext.get()).add(R.id.frame, QuestionsFragment.newInstance(arrayList.get(getAdapterPosition()).getName()))
                    .commitAllowingStateLoss();


        }

        void bind() {
            shadow.setVisibility(View.INVISIBLE);
            Speaker speaker = arrayList.get(getAdapterPosition());
            GlideApp.with(mContext.get()).load("https://firebasestorage.googleapis.com"+speaker.getProf_url()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    if (HeavilyUsed.isContextSafe(mContext.get())){
                      //  shadow.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            }).transition(DrawableTransitionOptions.withCrossFade(300)).centerCrop().into(speaker_image);
            speaker_session_name.setText("");
        }

    }

}
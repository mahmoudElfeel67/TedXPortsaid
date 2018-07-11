package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Model.Speaker;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    private final ArrayList<Speaker> arrayList;

    public SpeakerAdapter(AppCompatActivity editActivity,ArrayList arrayList) {
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

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        //@OnClick(R.id.root)
        public void onClick() {

           /* if (root.getHeight() == ints.dp2px(60, mContext.get())) {

                ResizeAnimations resizeAnimations = new ResizeAnimations(root, root.getWidth(), root.getHeight(), root.getWidth(), ints.dp2px(150, mContext.get()));
                resizeAnimations.setDuration(500);
                root.startAnimation(resizeAnimations);
                speaker_desc.animate().alpha(1).start();
            } else {
                ResizeAnimations resizeAnimations = new ResizeAnimations(root, root.getWidth(), root.getHeight(), root.getWidth(), ints.dp2px(60, mContext.get()));
                resizeAnimations.setDuration(500);
                root.startAnimation(resizeAnimations);
                speaker_desc.animate().alpha(0).start();
            }*/

        }

        void bind() {
            Speaker speaker = arrayList.get(getAdapterPosition());
            GlideApp.with(mContext.get()).load(speaker.getProf_url()).transition(DrawableTransitionOptions.withCrossFade(300)).centerCrop().into(speaker_image);
            speaker_session_name.setText(speaker.getName() +" - "+speaker.getTopic());
        }

    }

}
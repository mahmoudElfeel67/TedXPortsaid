package com.bloomers.tedxportsaid.Adapter;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.ResizeAnimations;
import com.bloomers.tedxportsaid.Utitltes.ints;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class SpeakerAdapter extends RecyclerView.Adapter<SpeakerAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;

    public SpeakerAdapter(AppCompatActivity editActivity) {
        this.mContext = new WeakReference<>(editActivity);
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
        return 100;
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        RelativeLayout root;

        @BindView(R.id.speaker_image)
        ImageView speaker_image;

        @BindView(R.id.speaker_session)
        TextView speaker_session;

        @BindView(R.id.speaker_title)
        TextView speaker_title;

        @BindView(R.id.speaker_desc)
        TextView speaker_desc;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.root)
        public void onClick() {

            if (root.getHeight() == ints.dp2px(60, mContext.get())) {

                ResizeAnimations resizeAnimations = new ResizeAnimations(root, root.getWidth(), root.getHeight(), root.getWidth(), ints.dp2px(150, mContext.get()));
                resizeAnimations.setDuration(500);
                root.startAnimation(resizeAnimations);
                speaker_desc.animate().alpha(1).start();
            } else {
                ResizeAnimations resizeAnimations = new ResizeAnimations(root, root.getWidth(), root.getHeight(), root.getWidth(), ints.dp2px(60, mContext.get()));
                resizeAnimations.setDuration(500);
                root.startAnimation(resizeAnimations);
                speaker_desc.animate().alpha(0).start();
            }

        }

        void bind() {
            if (getAdapterPosition() % 2 == 0) {
                speaker_desc.setTextColor(AppController.easyColor(mContext.get(), R.color.black));
                speaker_image.setImageResource(R.drawable.da7e7);
                root.setBackgroundResource(R.drawable.white_speaker_round);
                speaker_session.setTextColor(Color.parseColor("#e2646464"));
                speaker_title.setTextColor(AppController.easyColor(mContext.get(), R.color.black));
                speaker_title.setText("الدحيح :");
                speaker_session.setText("الانتروبي");

                root.getLayoutParams().width = (int) (ints.getScreenWidth(mContext.get()) * (0.90));
                root.getLayoutParams().height = ints.dp2px(60,mContext.get());
            } else {
                speaker_image.setImageResource(R.drawable.da7e7);
                root.setBackgroundResource(R.drawable.red_speaker_round);
                speaker_session.setTextColor(AppController.easyColor(mContext.get(), R.color.white));
                speaker_title.setTextColor(AppController.easyColor(mContext.get(), R.color.white));
                speaker_title.setText("الدحيح :");
                speaker_session.setText("السفاح");
                root.getLayoutParams().width = (int) (ints.getScreenWidth(mContext.get()) * (0.75));
                root.getLayoutParams().height = ints.dp2px(60,mContext.get());
                speaker_desc.setTextColor(AppController.easyColor(mContext.get(), R.color.white));


            }
        }

    }

}
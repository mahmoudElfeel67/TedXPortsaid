package com.bloomers.tedxportsaid.Adapter;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.Fragment.FormQuesionFragment;
import com.bloomers.tedxportsaid.Model.Speaker;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class AskSpeakerAdapter extends RecyclerView.Adapter<AskSpeakerAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    public Fragment fragment;
    private final android.support.v4.app.DialogFragment dialog;
    private final ArrayList<Speaker> arrayList;

    public AskSpeakerAdapter(AppCompatActivity editActivity, android.support.v4.app.DialogFragment dialog,ArrayList<Speaker> speaker) {
        this.mContext = new WeakReference<>(editActivity);
        this.dialog = dialog;
        this.arrayList =speaker;
    }

    @NonNull
    @Override
    public AskSpeakerAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item__ask_speaker, parent, false);
        return new AskSpeakerAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AskSpeakerAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.speaker_image)
        ImageView speaker_image;

        @BindView(R.id.fab)
        FloatingActionButton fab;


        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.card)
        public void onClick() {
            fragment = FormQuesionFragment.newInstance(arrayList.get(getAdapterPosition()));

            FragmentTransaction ft = dialog.getChildFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fade_in,
                 R.anim.fade_out,
                 R.anim.fade_out,
                 R.anim.fade_out);
            ft.add(R.id.ask_root, fragment)
                 .addToBackStack("tag")
                 .commitAllowingStateLoss();
        }

        void bind() {
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ca302c")));
            Speaker speaker = arrayList.get(getAdapterPosition());
            GlideApp.with(mContext.get()).load("https://firebasestorage.googleapis.com"+speaker.getProf_url()).transition(DrawableTransitionOptions.withCrossFade(300)).into(speaker_image);
        }

    }

}

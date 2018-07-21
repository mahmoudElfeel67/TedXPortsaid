package com.bloomers.tedxportsaid.Adapter;


import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Fragment.FormQuesionFragment;
import com.bloomers.tedxportsaid.Model.Speaker;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
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

        @BindView(R.id.speaker_name)
        TextView speaker_name;

        @BindView(R.id.speaker_image)
        ImageView speaker_image;

        @BindView(R.id.shadow)
                View shadow;

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
                        shadow.setVisibility(View.VISIBLE);
                    }
                    return false;
                }
            }).transition(DrawableTransitionOptions.withCrossFade(300)).centerCrop().into(speaker_image);
            speaker_name.setText(speaker.getName() +" - "+speaker.getTopic());
        }

    }

}

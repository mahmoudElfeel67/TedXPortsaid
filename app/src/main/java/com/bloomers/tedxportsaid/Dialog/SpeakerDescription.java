package com.bloomers.tedxportsaid.Dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Model.Speaker;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SpeakerDescription extends DialogFragment {

    private Speaker speaker;
    @BindView(R.id.speaker_image) ImageView speakerImage;
    @BindView(R.id.speaker_desc) TextView speakerDesc;

    public static SpeakerDescription newInstance(Speaker speaker){
        SpeakerDescription SpeakerDescription = new SpeakerDescription();
        SpeakerDescription.speaker = speaker;
        return SpeakerDescription;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speaker_desc, container);

        ButterKnife.bind(this,view);

        if (getDialog()!=null&&getDialog().getWindow()!=null) {
            getDialog().getWindow().setWindowAnimations(R.style.MyAnimation_Window);

        }


        GlideApp.with(getActivity()).load("https://firebasestorage.googleapis.com"+speaker.getProf_url()).transition(DrawableTransitionOptions.withCrossFade(300)).into(speakerImage);
        speakerDesc.setText(speaker.getDescription());
        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }


}
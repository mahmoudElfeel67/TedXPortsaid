package com.bloomers.tedxportsaid.Dialog;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Adapter.AskSpeakerAdapter;
import com.bloomers.tedxportsaid.Model.Speaker;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GridLayoutManagerEXT;

import java.util.ArrayList;

public class AskSpeakerDialog extends DialogFragment {

    private ArrayList<Speaker> speakers;

    public static AskSpeakerDialog newInstance(ArrayList<Speaker> speaker){
        AskSpeakerDialog askSpeakerDialog = new AskSpeakerDialog();
        askSpeakerDialog.speakers = speaker;
        return askSpeakerDialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_spearker, container);
        final AskSpeakerAdapter askSpeakerAdapter = new AskSpeakerAdapter((AppCompatActivity) getActivity(),this,speakers);

        if (getDialog()!=null&&getDialog().getWindow()!=null){
            getDialog().getWindow().setWindowAnimations(R.style.MyAnimation_Window);

            getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            if (askSpeakerAdapter.fragment!=null){
                                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,
                                     R.anim.fade_out,
                                     R.anim.fade_out,
                                     R.anim.fade_out).remove(askSpeakerAdapter.fragment).commitAllowingStateLoss();
                                askSpeakerAdapter.fragment = null;
                            }else {
                                dismissAllowingStateLoss();
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
        }


        RecyclerView article_recycler = view.findViewById(R.id.ask_speakers_recycler);
        article_recycler.setLayoutManager(new GridLayoutManagerEXT(getContext(), 2));
        article_recycler.setAdapter(askSpeakerAdapter);


        return view;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
    }


}
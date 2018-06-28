package com.bloomers.tedxportsaid.Dialog;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.Manager.RandomThreadManager;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.bloomers.tedxportsaid.Utitltes.ints;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.bloomers.tedxportsaid.Utitltes.other.delay;
import com.github.ybq.android.spinkit.SpinKitView;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class RandomDialog extends DialogFragment {


    @BindView(R.id.random_number) TextView random_number;
    @BindView(R.id.articleLayout) RelativeLayout articleLayout;
    @BindView(R.id.articleImage) ImageView articleImage;
    @BindView(R.id.article_text) TextView article_text;
    @BindView(R.id.actionButton) Button actionButton;
    @BindView(R.id.spinner) SpinKitView spinKitView;
    @BindView(R.id.circle_source) ImageView circle_source;
    boolean isArticle;
    static List arrayList;
   public int random;
   public static YouTubeVideo youTubeVideo;


    public static RandomDialog newInstance(Boolean isArticle, List arrayList){

        RandomDialog randomDialog = new RandomDialog();
        randomDialog.isArticle= isArticle;
        randomDialog.arrayList = arrayList;
        randomDialog.youTubeVideo = null;
        return randomDialog;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_random, container);
        ButterKnife.bind(this,view);
        if (getDialog()!=null&&getDialog().getWindow()!=null) {
            getDialog().getWindow().setWindowAnimations(R.style.MyAnimation_Window);
            getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);

        }


        if (youTubeVideo==null){
            RandomThreadManager.getInstance().addNumberChanger(random_number, getActivity(), new RandomThreadManager.onDone() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onDone(int random) {
                    RandomDialog.this.random = random;
                    if (HeavilyUsed.isContextSafe(getActivity())&&getContext()!=null){
                        spinKitView.setVisibility(View.GONE);
                        if (isArticle){

                        }else {
                            intilizeVidoe();
                        }
                        new delay(random_number, 1000) {
                            @Override
                            protected void OnDelayEnded() {
                                if (getContext()!=null){
                                    articleLayout.animate().setDuration(1000).translationX(0).start();
                                    random_number.animate().setDuration(1000).translationX(ints.dp2px(250,getContext()));
                                }
                            }
                        };
                    }

                }
            },arrayList.size());
        }else {
            articleLayout.setTranslationX(0);
            random_number.setTranslationX(ints.dp2px(250,getContext()));
            articleLayout.requestLayout();
            random_number.requestLayout();
            intilizeVidoe();
        }





        return view;
    }

    private void intilizeVidoe() {
        youTubeVideo = ((ArrayList<YouTubeVideo>)arrayList).get(random);
        GlideApp.with(getActivity()).load(((ArrayList<YouTubeVideo>)arrayList).get(random).getThumbnailUrl()).centerCrop().into(articleImage);
        article_text.setText(((ArrayList<YouTubeVideo>)arrayList).get(random).getTitle());
        actionButton.setText(R.string.watch);

        circle_source.setVisibility(View.GONE);
    }

    @OnClick(R.id.actionButton)
    void onClick(){
        if (getActivity()==null)return;

        if (isArticle){
            new FinestWebView.Builder(getActivity())
                 .iconDefaultColor(Color.WHITE)
                 .titleColor(Color.WHITE)
                 .urlColor(Color.WHITE)
                 .statusBarColor(AppController.easyColor(getActivity(),R.color.statusBarColor))
                 .toolbarColor(AppController.easyColor(getActivity(),R.color.colorAccent))
                 .show("https://www.youtube.com/watch?v=dARAN1z2KqY");
        }else {
            HeavilyUsed.callSaveDialog((AppCompatActivity) getActivity(), YoutubeFragment.newInstance(((ArrayList<YouTubeVideo>)arrayList).get(random).getId()),null);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RandomThreadManager.getInstance().stopRandom();
    }
}
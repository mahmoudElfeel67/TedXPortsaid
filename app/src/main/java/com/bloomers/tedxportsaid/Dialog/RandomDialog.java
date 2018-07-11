package com.bloomers.tedxportsaid.Dialog;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.thefinestartist.finestwebview.FinestWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bloomers.tedxportsaid.AppController.parsePageHeaderInfo;

public class RandomDialog extends DialogFragment {


    @BindView(R.id.random_number)  TextView random_number;
    @BindView(R.id.articleLayout)  RelativeLayout articleLayout;
    @BindView(R.id.articleImage)  ImageView articleImage;
    @BindView(R.id.article_text)  TextView article_text;
    @BindView(R.id.actionButton)  Button actionButton;
    @BindView(R.id.spinner)  SpinKitView spinKitView;
    @BindView(R.id.circle_source)  ImageView circle_source;
    private boolean isArticle;
    private static List arrayList;
   private int random;
   private static YouTubeVideo youTubeVideo;


    public static RandomDialog newInstance(Boolean isArticle, List arrayList){

        RandomDialog randomDialog = new RandomDialog();
        randomDialog.isArticle= isArticle;
        RandomDialog.arrayList = arrayList;
        youTubeVideo = null;
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
                public void onDone(final int random) {
                    RandomDialog.this.random = random;
                    if (HeavilyUsed.isContextSafe(getActivity())&&getContext()!=null){
                        spinKitView.setVisibility(View.GONE);
                        if (isArticle){
                            GlideApp.with(getActivity()).load(R.drawable.fav_ted).transition(DrawableTransitionOptions.withCrossFade(300)).into(circle_source);
                           @SuppressLint("StaticFieldLeak") AsyncTask asyncTask =    new AsyncTask<Pair, Pair, Pair>() {
                                @Override
                                protected Pair doInBackground(Pair... objects) {

                                    try {
                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("TEDX", Context.MODE_PRIVATE);
                                        String url = (String) arrayList.get(random);
                                        String extracted;
                                        String header;

                                        if (sharedPreferences.getString(url, "NNN").equals("NNN")  || sharedPreferences.getString(url + "title", "NNN").equals("NNN")) {

                                            Document doc = Jsoup.connect(url).get();
                                            header = doc.title();
                                            extracted = parsePageHeaderInfo(doc);


                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(url, extracted);
                                            editor.putString(url + "title", header);
                                            editor.apply();
                                        } else {
                                            extracted = sharedPreferences.getString(url, "NNN");
                                            header = sharedPreferences.getString(url + "title", "NNN");
                                        }
                                        return new Pair(extracted, header);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                }

                                @Override
                                protected void onPostExecute(Pair o) {
                                    super.onPostExecute(o);

                                    try {
                                        GlideApp.with(getActivity()).load(o.first).transition(DrawableTransitionOptions.withCrossFade(300)).centerCrop().into(articleImage);
                                        article_text.setText((CharSequence) o.second);
                                        spinKitView.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.execute();
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
                 .show((String) arrayList.get(random));
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
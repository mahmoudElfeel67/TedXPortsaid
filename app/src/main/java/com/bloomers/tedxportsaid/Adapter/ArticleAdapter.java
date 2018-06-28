package com.bloomers.tedxportsaid.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.FaviconParser;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.pressTouchListener;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.thefinestartist.finestwebview.FinestWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;

    public ArticleAdapter(AppCompatActivity editActivity) {
        this.mContext = new WeakReference<>(editActivity);
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {

        return 100;
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.circle_source) ImageView circle_source;
        @BindView(R.id.articleImage) ImageView articleImage;
        @BindView(R.id.article_text) TextView articleText;
        @BindView(R.id.spinner) SpinKitView spinner;
        @BindView(R.id.card) CardView cardView;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);
            cardView.setOnTouchListener(pressTouchListener);

        }

        final String art = "http://www.ted.com/about/programs-initiatives/tedx-program";

        String parsePageHeaderInfo(Document doc) {

            Elements elements = doc.select("meta");
            String imageUrl = null;

            for (Element e : elements) {
                //fetch image url from content attribute of meta tag.
                imageUrl = e.attr("content");

                //OR more specifically you can check meta property.
                if (e.attr("property").equalsIgnoreCase("og:image")) {
                    imageUrl = e.attr("content");
                    break;
                }
            }
            return imageUrl;
        }


        final pressTouchListener pressTouchListener = new pressTouchListener() {
            @Override
            protected void OnClick(View view) {

                new FinestWebView.Builder(mContext.get())
                        .iconDefaultColor(Color.WHITE)
                        .titleColor(Color.WHITE)
                        .urlColor(Color.WHITE)
                        .statusBarColor(AppController.easyColor(mContext.get(), R.color.statusBarColor))
                        .toolbarColor(AppController.easyColor(mContext.get(), R.color.colorAccent))
                        .show(art);
            }
        };

        @SuppressLint("StaticFieldLeak")
        void bind() {

            new AsyncTask<Pair, Pair, Pair>() {
                @Override
                protected Pair doInBackground(Pair... objects) {

                    try {
                        SharedPreferences sharedPreferences = mContext.get().getSharedPreferences("TEDX", Context.MODE_PRIVATE);
                        String url = art;
                        String extracted;
                        final String fav;
                        String header;

                        if (sharedPreferences.getString(url, "NNN").equals("NNN") || sharedPreferences.getString(url + "fav", "NNN").equals("NNN") || sharedPreferences.getString(url + "title", "NNN").equals("NNN")) {

                            Document doc = Jsoup.connect(url).get();
                            header = doc.title();
                            extracted = parsePageHeaderInfo(doc);

                            FaviconParser faviconParser = new FaviconParser();
                            fav = faviconParser.startFaviconSearch(doc, art);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(url, extracted);
                            editor.putString(url + "fav", fav);
                            editor.putString(url + "title", header);
                            editor.apply();
                        } else {
                            extracted = sharedPreferences.getString(url, "NNN");
                            fav = sharedPreferences.getString(url + "fav", "NNN");
                            header = sharedPreferences.getString(url + "title", "NNN");
                        }
                        return new Pair(new Pair(extracted, header), fav);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Pair o) {
                    super.onPostExecute(o);
                    
                    try {
                        GlideApp.with(mContext.get()).load((String) ((Pair) o.first).first).transition(DrawableTransitionOptions.withCrossFade(300)).centerCrop().into(articleImage);
                        GlideApp.with(mContext.get()).load((String) o.second).transition(DrawableTransitionOptions.withCrossFade(300)).into(circle_source);
                        articleText.setText((String) ((Pair) o.first).second);
                        spinner.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();

        }

    }

}

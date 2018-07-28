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
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.pressTouchListener;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.ybq.android.spinkit.SpinKitView;
import com.thefinestartist.finestwebview.FinestWebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bloomers.tedxportsaid.AppController.parsePageHeaderInfo;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    private final ArrayList<String> articles;

    public ArticleAdapter(AppCompatActivity editActivity, ArrayList<String> arrayList) {
        this.mContext = new WeakReference<>(editActivity);
        this.articles = arrayList;
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
    public void onViewRecycled(@NonNull SingleItemRowHolder holder) {
        super.onViewRecycled(holder);
        holder.recycle();
    }

    @Override
    public int getItemCount() {

        return articles.size();
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.circle_source) ImageView circle_source;
        @BindView(R.id.articleImage) ImageView articleImage;
        @BindView(R.id.article_text) TextView articleText;
        @BindView(R.id.spinner) SpinKitView spinner;
        @BindView(R.id.card) CardView cardView;
        AsyncTask asyncTask;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);
            cardView.setOnTouchListener(pressTouchListener);

        }

        String art;


        final pressTouchListener pressTouchListener = new pressTouchListener() {
            @Override
            protected void OnClick(View view) {

                new FinestWebView.Builder(mContext.get())
                        .iconDefaultColor(Color.WHITE)
                        .titleColor(Color.WHITE)
                        .urlColor(Color.WHITE)
                        .statusBarColor(AppController.easyColor(mContext.get(), R.color.main_bacground_clolor))
                        .toolbarColor(AppController.easyColor(mContext.get(), R.color.main_bacground_clolor))
                        .show(art);
            }
        };

        @SuppressLint("StaticFieldLeak")
        void bind() {
            art = articles.get(getAdapterPosition());
            GlideApp.with(mContext.get()).load(R.drawable.fav_ted).transition(DrawableTransitionOptions.withCrossFade(300)).into(circle_source);
            spinner.setVisibility(View.VISIBLE);
            GlideApp.with(mContext.get()).clear(articleImage);
            articleText.setText("");
            if (asyncTask != null) {
                AppController.getInstance().cancelAsync(asyncTask);
            }
            asyncTask = new AsyncTask<Pair, Pair, Pair>() {
                @Override
                protected Pair doInBackground(Pair... objects) {

                    try {
                        SharedPreferences sharedPreferences = mContext.get().getSharedPreferences("TEDX", Context.MODE_PRIVATE);
                        String url = art;
                        String extracted;
                        String header;

                        if (sharedPreferences.getString(url, "NNN").equals("NNN") || sharedPreferences.getString(url + "title", "NNN").equals("NNN")) {

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
                        GlideApp.with(mContext.get()).load(o.first).transition(DrawableTransitionOptions.withCrossFade(300)).centerCrop().into(articleImage);
                        articleText.setText((CharSequence) o.second);
                        spinner.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();

        }

        void recycle() {
            if (asyncTask != null) {
                AppController.getInstance().cancelAsync(asyncTask);
            }
        }
    }

}

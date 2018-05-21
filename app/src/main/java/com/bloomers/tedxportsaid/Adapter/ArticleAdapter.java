package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
public class ArticleAdapter  extends RecyclerView.Adapter<ArticleAdapter.SingleItemRowHolder> {

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
        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }



        public void onClick() {



        }

        void bind() {
            GlideApp.with(mContext.get()).load(R.drawable.circle_morty).circleCrop().into(circle_source);
        }

    }

}

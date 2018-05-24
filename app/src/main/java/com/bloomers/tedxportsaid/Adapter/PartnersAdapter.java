package com.bloomers.tedxportsaid.Adapter;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.thefinestartist.finestwebview.FinestWebView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class PartnersAdapter extends RecyclerView.Adapter<PartnersAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;

    public PartnersAdapter(AppCompatActivity editActivity) {
        this.mContext = new WeakReference<>(editActivity);
    }

    @NonNull
    @Override
    public PartnersAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner, parent, false);
        return new PartnersAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnersAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.partenr_logo)
        ImageView partenr_logo;


        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }



        @OnClick(R.id.partenr_logo)
        public void onClick() {
            new FinestWebView.Builder(mContext.get())
                 .iconDefaultColor(Color.WHITE)
                 .titleColor(Color.WHITE)
                 .urlColor(Color.WHITE)
                 .statusBarColor(AppController.easyColor(mContext.get(),R.color.statusBarColor))
                 .toolbarColor(AppController.easyColor(mContext.get(),R.color.colorAccent))
                 .show("https://www.youtube.com/watch?v=dARAN1z2KqY");


        }

        void bind() {
            GlideApp.with(mContext.get()).load(R.drawable.cn_logo).centerInside().into(partenr_logo);
        }

    }

}
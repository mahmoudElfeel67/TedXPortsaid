package com.bloomers.tedxportsaid.Adapter;


import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class ArticleSegmentAdapter extends RecyclerView.Adapter<ArticleSegmentAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    public static int clicked = 0;
    private final ArrayList<Integer> arrayList = new ArrayList<>();
    private final onClicked onClicked;

    public ArticleSegmentAdapter(AppCompatActivity editActivity,onClicked onClicked) {
        this.mContext = new WeakReference<>(editActivity);
        this.onClicked= onClicked;
        arrayList.add(R.string.technology);
        arrayList.add(R.string.Education);
        arrayList.add(R.string.Design);
        arrayList.add(R.string.Entertainment);
        arrayList.add(R.string.Ideas);
        arrayList.add(R.string.Cultures);
    }

    @NonNull
    @Override
    public ArticleSegmentAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_segment, parent, false);
        return new ArticleSegmentAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleSegmentAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.segment_name) TextView segment_name;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

        @OnClick(R.id.segment_name)
        void onClick() {
            onClicked.onClick(getAdapterPosition());
            clicked = getAdapterPosition();
            notifyDataSetChanged();

        }

        void bind() {
            if (getAdapterPosition() == clicked) {
                segment_name.setTextColor(Color.BLACK);
                segment_name.setTypeface(Typeface.createFromAsset(mContext.get().getAssets(), AppController.bigFont));
            } else {
                segment_name.setTextColor(Color.parseColor("#ab585858"));
                segment_name.setTypeface(Typeface.createFromAsset(mContext.get().getAssets(), AppController.smallFont));
            }

            segment_name.setText(arrayList.get(getAdapterPosition()));

        }

    }

    public interface onClicked{
        void  onClick(int postion);
    }

}
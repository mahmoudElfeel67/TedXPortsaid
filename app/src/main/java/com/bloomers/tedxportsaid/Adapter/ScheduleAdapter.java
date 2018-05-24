package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.ints;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.SingleItemRowHolder> {

    private final WeakReference<AppCompatActivity> mContext;
    int leftPostion = 0;
    int rightPostion = 0;

    public ScheduleAdapter(AppCompatActivity editActivity) {
        this.mContext = new WeakReference<>(editActivity);
    }

    @NonNull
    @Override
    public ScheduleAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 5) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_reverse, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        }
        return new ScheduleAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 5;
        } else {
            return 0;
        }
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.top_line) View top_line;
        @BindView(R.id.bottom_line) View bottom_line;
        @BindView(R.id.event_time) ImageView event_time;
        @BindView(R.id.lay) RelativeLayout lay;
        @BindView(R.id.sch_desc) TextView sch_desc;
        @BindView(R.id.event_container) RelativeLayout event_container;

        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void onClick() {

        }

        void bind() {
            GlideApp.with(mContext.get()).load(R.drawable.even_time).circleCrop().into(event_time);

            event_container.requestLayout();
            event_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (leftPostion == 0) {
                        leftPostion = ((ints.getScreenWidth(mContext.get()) / 2) - (ints.dp2px(217.5F, mContext.get()) / 2)) - ints.dp2px(30, mContext.get());
                    }

                    if (rightPostion == 0) {
                        rightPostion = ((ints.getScreenWidth(mContext.get()) / 2) + (ints.dp2px(217.5F, mContext.get()) / 2)) - ints.dp2px(85, mContext.get());
                    }

                    if (getAdapterPosition() % 2 == 0) {
                        event_container.setX(leftPostion);
                        event_container.requestLayout();

                    } else {
                        event_container.setX(rightPostion);
                        event_container.requestLayout();
                    }
                }
            });

            if (getAdapterPosition() == 0) {
                top_line.setVisibility(View.GONE);
                lay.setPadding(0, ints.dp2px(10, mContext.get()), 0, 0);
                sch_desc.setText("فتح البوابات\n \n 7 pm");
            } else {
                top_line.setVisibility(View.VISIBLE);
                lay.setPadding(0, ints.dp2px(10, mContext.get()), 0, 0);
                sch_desc.setText("بدايه محاضره ريك");
            }

        }

    }

}
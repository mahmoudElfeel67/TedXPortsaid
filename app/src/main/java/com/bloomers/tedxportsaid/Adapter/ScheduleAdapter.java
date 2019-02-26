package com.bloomers.tedxportsaid.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Model.Schedule;
import com.bloomers.tedxportsaid.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.SingleItemRowHolder> {

    private final ArrayList<Schedule> schedules;

    public ScheduleAdapter(ArrayList schedules) {
        this.schedules= schedules;
    }

    @NonNull
    @Override
    public ScheduleAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleAdapter.SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.SingleItemRowHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.desc) TextView desc;
        @BindView(R.id.date) TextView date;


        SingleItemRowHolder(@NonNull final View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void onClick() {

        }

        void bind() {
            Schedule schedule = schedules.get(getAdapterPosition());
            desc.setText(schedule.getSession_name());
            date.setText(schedule.getDate());

        }

    }

}
package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bloomers.tedxportsaid.Adapter.ScheduleAdapter;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.Model.Schedule;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventTimeLineFragment extends Fragment {

    public static EventTimeLineFragment newInstance() {
        return new EventTimeLineFragment();
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private View root;
    private LinearLayout no_data;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        root = inflater.inflate(R.layout.fragment_timeline, container, false);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        no_data = root.findViewById(R.id.no_data);
        loadSchedule();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadSchedule();
            }
        });


        return root;
    }

    private void loadSchedule() {
        if (AppController.getInstance().isThereInternet(getActivity())) {
            read();
        } else {
            no_data.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            read();
        }
    }

    private void read() {
        FirebaseDatabase.getInstance().getReference().child("schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null&&dataSnapshot.exists()) {
                    ArrayList<Schedule> arrayList = new ArrayList();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Schedule speaker = dataSnapshot1.getValue(Schedule.class);
                        arrayList.add(speaker);
                    }

                    RecyclerView article_segment_recycler = root.findViewById(R.id.schedle_recycler);
                    article_segment_recycler.setLayoutManager(new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false));
                    article_segment_recycler.setAdapter(new ScheduleAdapter(arrayList));
                    swipeRefreshLayout.setRefreshing(false);
                    no_data.setVisibility(View.GONE);
                } else {
                    no_data.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setRefreshing(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                no_data.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
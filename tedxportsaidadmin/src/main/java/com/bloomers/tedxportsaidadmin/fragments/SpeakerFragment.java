package com.bloomers.tedxportsaidadmin.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bloomers.tedxportsaidadmin.Adapter.SpeakerAdapter;
import com.bloomers.tedxportsaidadmin.AppController;
import com.bloomers.tedxportsaidadmin.R;
import com.bloomers.tedxportsaidadmin.Utitltes.other.GridLayoutManagerEXT;
import com.bloomers.tedxportsaidadmin.model.Speaker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SpeakerFragment extends Fragment {

    public static SpeakerFragment newInstance() {
        return new SpeakerFragment();
    }

    private View root;
    private LinearLayout no_data;
    public  static  ArrayList<Speaker> speakers;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        root = inflater.inflate(R.layout.fragment_speakers, container, false);
        no_data = root.findViewById(R.id.no_data);

        loadSpeakers();

        ((SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadSpeakers();
            }
        });


        return root;
    }

    private void loadSpeakers() {

        if (AppController.getInstance().isThereInternet(getActivity())){
            read();
        }else {
            no_data.setVisibility(View.VISIBLE);
            ((SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
            read();
        }

    }

    private void read() {
        FirebaseDatabase.getInstance().getReference().child("Speakers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null&&dataSnapshot.exists()) {
                    ArrayList<Speaker> arrayList = new ArrayList();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Speaker speaker = dataSnapshot1.getValue(Speaker.class);
                        arrayList.add(speaker);
                    }
                    speakers = arrayList;

                    RecyclerView article_segment_recycler = root.findViewById(R.id.RecyclerSpeaker);
                    article_segment_recycler.setLayoutManager(new GridLayoutManagerEXT(getContext(), 2));
                    article_segment_recycler.setAdapter(new SpeakerAdapter(getActivity(), arrayList));
                    ((SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                    no_data.setVisibility(View.GONE);

                } else {
                    no_data.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                no_data.setVisibility(View.VISIBLE);
                ((SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
            }
        });
    }

}
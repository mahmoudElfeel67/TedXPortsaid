package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bloomers.tedxportsaid.Adapter.PartnersAdapter;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.Model.Partner;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GridLayoutManagerEXT;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PartnersFragment extends Fragment {

    public static PartnersFragment newInstance() {
        return new PartnersFragment();
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView article_recycler;
    private LinearLayout no_data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_partners, container, false);
        article_recycler = root.findViewById(R.id.partenrs_recycler);
        swipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);
        no_data = root.findViewById(R.id.no_data);

        GridLayoutManagerEXT layoutManager = new GridLayoutManagerEXT(getActivity(), 3);
        article_recycler.setLayoutManager(layoutManager);

        loadPartners();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPartners();
            }
        });

        return root;
    }

    private void loadPartners() {
        if (AppController.getInstance().isThereInternet(getActivity())) {
            FirebaseDatabase.getInstance().getReference().child("Partners").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        ArrayList<Partner> arrayList = new ArrayList();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Partner speaker = dataSnapshot1.getValue(Partner.class);
                            arrayList.add(speaker);
                        }

                        article_recycler.setAdapter(new PartnersAdapter((AppCompatActivity) getActivity(), arrayList));
                        swipeRefreshLayout.setRefreshing(false);
                        no_data.setVisibility(View.GONE);

                    }else {
                        swipeRefreshLayout.setRefreshing(false);
                        no_data.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    swipeRefreshLayout.setRefreshing(false);
                    no_data.setVisibility(View.VISIBLE);
                }
            });
        } else {
            swipeRefreshLayout.setRefreshing(false);
            no_data.setVisibility(View.VISIBLE);
        }
    }

}
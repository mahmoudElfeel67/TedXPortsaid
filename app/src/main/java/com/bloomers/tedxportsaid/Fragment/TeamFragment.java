package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bloomers.tedxportsaid.Adapter.TeamAdapter;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.Model.TeamMember;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GridLayoutManagerEXT;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamFragment extends Fragment {

    public static TeamFragment newInstance(onCLick onCLick) {

        TeamFragment teamFragment = new TeamFragment();
        teamFragment.clicked = onCLick;
        return teamFragment;
    }


    private RecyclerView article_recycler;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SpinKitView spin_kit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_team, container, false);
        article_recycler = root.findViewById(R.id.team_recycler);
        swipeRefreshLayout =root.findViewById(R.id.swipeRefreshLayout);
        spin_kit = root.findViewById(R.id.spin_kit);

        GridLayoutManagerEXT layoutManager = new GridLayoutManagerEXT(getActivity(), 12);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                switch (position) {
                    // first three items span 3 columns each
                    case 0:
                    case 8:
                    case 21:
                    case 29:
                    case 40:
                    case 49:
                        return 12;
                }


                return  3;

            }
        });
        article_recycler.setNestedScrollingEnabled(false);
        article_recycler.setLayoutManager(layoutManager);

        loadTeam();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadTeam();

            }
        });




        ((TextView)root.findViewById(R.id.ted)).setText("TED\n" +
                "\n" +
                "TED is a nonprofit devoted to spreading ideas, usually in the form of short, powerful talks (18 minutes or less). TED began in 1984 as a conference where Technology, Entertainment and Design converged, and today covers almost all topics — from science to business to global issues — in more than 100 languages. Meanwhile, independently run TEDx events help share ideas in communities around the world.\n" +
                "\n" +
                "\n" +
                "TEDx\n" +
                "\n" +
                "A TEDx event is a local gathering where live TED-like talks and videos previously recorded at TED conferences are shared with the community. TEDx events are fully planned and coordinated independently, on a community-by-community basis. The content and design of each TEDx event is unique and developed independently, but all of them have features in common."
                );
        return root;
    }

    private void loadTeam() {
        if (AppController.getInstance().isThereInternet(getActivity())){
            read();
        }else{
            swipeRefreshLayout.setRefreshing(false);
            spin_kit.setVisibility(View.VISIBLE);
            read();
        }
    }

    private void read() {
        FirebaseDatabase.getInstance().getReference().child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null&&dataSnapshot.getValue()!=null&&dataSnapshot.exists()){
                    ArrayList<TeamMember> arrayList = new ArrayList();
                    arrayList.add(new TeamMember());
                    for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                        TeamMember teamMember = dataSnapshot1.getValue(TeamMember.class);
                        if (teamMember.getName().toLowerCase().equals("Ahmed Abd El-MA'Boud".toLowerCase())
                                ||teamMember.getName().toLowerCase().equals("Shahdenda Shalaby".toLowerCase())
                                ||teamMember.getName().toLowerCase().equals("OMNIA GABR".toLowerCase())
                                ||teamMember.getName().toLowerCase().equals("Rana Maher Lamiey".toLowerCase())
                                ||teamMember.getName().toLowerCase().equals("Shoroq Shams".toLowerCase())){
                            arrayList.add(new TeamMember());
                        }


                        arrayList.add(teamMember);
                    }

                    article_recycler.setAdapter(new TeamAdapter((AppCompatActivity) getActivity(),clicked,arrayList));
                    swipeRefreshLayout.setRefreshing(false);
                    spin_kit.setVisibility(View.GONE);
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                    spin_kit.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
                spin_kit.setVisibility(View.VISIBLE);
            }
        });
    }

    public interface onCLick{
        void onClick(View view, TeamMember teamMember);
    }

    private onCLick clicked;

}
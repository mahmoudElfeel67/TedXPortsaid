package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
                // 7 is the sum of items in one repeated section
                switch (position % 7) {
                    // first three items span 3 columns each
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        return 3;
                    // next four items span 2 columns each


                    case 4:
                    case 5:
                    case 6:
                        return 4;
                }
                throw new IllegalStateException("internal error");
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




        ((TextView)root.findViewById(R.id.tedx)).setText(Html.fromHtml("<html>\n" +
             "<body>\n" +
             "\n" +
             "<p><font color=\"red\">TEDx</font></p>\n" +
             "<p><font color=\"black\">كذاوكذا وكذا وبلا بلا بلا</font></p>\n" +
             "\n" +
             "\n" +
             "</body>\n" +
             "</html>\n"));


        ((TextView)root.findViewById(R.id.ted)).setText(Html.fromHtml("<html>\n" +
             "<body>\n" +
             "\n" +
             "<p><font color=\"red\">TED</font></p>\n" +
             "<p><font color=\"black\">كذاوكذا وكذا وبلا بلا بلا</font></p>\n" +
             "\n" +
             "\n" +
             "</body>\n" +
             "</html>\n"));
        return root;
    }

    private void loadTeam() {
        if (AppController.getInstance().isThereInternet(getActivity())){
            FirebaseDatabase.getInstance().getReference().child("Team").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot!=null&&dataSnapshot.getValue()!=null&&dataSnapshot.exists()){
                        ArrayList<TeamMember> arrayList = new ArrayList();
                        for (DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){
                            TeamMember speaker = dataSnapshot1.getValue(TeamMember.class);
                            arrayList.add(speaker);
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
        }else{
            swipeRefreshLayout.setRefreshing(false);
            spin_kit.setVisibility(View.VISIBLE);
        }
    }

    public interface onCLick{
        void onClick(View view, TeamMember teamMember);
    }

    private onCLick clicked;

}
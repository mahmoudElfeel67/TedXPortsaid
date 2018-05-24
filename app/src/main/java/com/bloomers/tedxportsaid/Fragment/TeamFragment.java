package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Adapter.TeamAdapter;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GridLayoutManagerEXT;
public class TeamFragment extends Fragment {

    public static TeamFragment newInstance(onCLick onCLick) {
        TeamFragment teamFragment = new TeamFragment();
        teamFragment.clicked = onCLick;
        return teamFragment;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_team, container, false);
        RecyclerView article_recycler = root.findViewById(R.id.team_recycler);

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
                        return 4;
                    // next four items span 2 columns each

                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        return 3;
                }
                throw new IllegalStateException("internal error");
            }
        });

        article_recycler.setLayoutManager(layoutManager);
        article_recycler.setAdapter(new TeamAdapter((AppCompatActivity) getActivity(),clicked));
        return root;
    }

    public interface onCLick{
        void onClick(View view);
    }

    private onCLick clicked;

}
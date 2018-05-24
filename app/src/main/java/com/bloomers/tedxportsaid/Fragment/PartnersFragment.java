package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Adapter.PartnersAdapter;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GridLayoutManagerEXT;
public class PartnersFragment extends Fragment {

    public static PartnersFragment newInstance() {
        return new PartnersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_partners, container, false);
        RecyclerView article_recycler = root.findViewById(R.id.partenrs_recycler);

        GridLayoutManagerEXT layoutManager = new GridLayoutManagerEXT(getActivity(), 3);
        article_recycler.setLayoutManager(layoutManager);
        article_recycler.setAdapter(new PartnersAdapter((AppCompatActivity) getActivity()));
        return root;
    }

}
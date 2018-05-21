package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Adapter.ArticleAdapter;
import com.bloomers.tedxportsaid.Adapter.ArticleSegmentAdapter;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GridLayoutManagerEXT;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;
public class ArticleFragment extends Fragment {

    public static ArticleFragment newInstance() {
        return new ArticleFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_article, container, false);

        RecyclerView article_segment_recycler = root.findViewById(R.id.article_segment_recycler);
        article_segment_recycler.setLayoutManager(new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.HORIZONTAL, false));
        article_segment_recycler.setAdapter(new ArticleSegmentAdapter((AppCompatActivity) getActivity()));


        RecyclerView article_recycler = root.findViewById(R.id.article_recycler);
        article_recycler.setLayoutManager(new GridLayoutManagerEXT(getContext(), 2));
        article_recycler.setAdapter(new ArticleAdapter((AppCompatActivity) getActivity()));

        return root;
    }

}
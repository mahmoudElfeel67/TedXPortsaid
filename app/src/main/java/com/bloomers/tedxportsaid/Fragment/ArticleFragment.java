package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Adapter.ArticleAdapter;
import com.bloomers.tedxportsaid.Adapter.ArticleSegmentAdapter;
import com.bloomers.tedxportsaid.Model.Article;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.GridLayoutManagerEXT;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;

import java.util.ArrayList;

public class ArticleFragment extends Fragment {

    public static ArticleFragment newInstance() {
        return new ArticleFragment();
    }
    
    public static ArrayList<String> articles;
    private RecyclerView article_recycler;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_article, container, false);
        article_recycler = root.findViewById(R.id.article_recycler);

        RecyclerView  article_segment_recycler = root.findViewById(R.id.article_segment_recycler);
        article_segment_recycler.setLayoutManager(new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.HORIZONTAL, false));
        article_segment_recycler.setAdapter(new ArticleSegmentAdapter((AppCompatActivity) getActivity(), new ArticleSegmentAdapter.onClicked() {
            @Override
            public void onClick(int postion) {
                setArticles(postion);
            }
        }));

        ((SwipeRefreshLayout)root.findViewById(R.id.swipeRefreshLayout)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((SwipeRefreshLayout)root.findViewById(R.id.swipeRefreshLayout)).setRefreshing(false);
                setArticles(ArticleSegmentAdapter.clicked);

            }
        });



        article_recycler.setLayoutManager(new GridLayoutManagerEXT(getContext(), 2));
        article_recycler.setAdapter(new ArticleAdapter((AppCompatActivity) getActivity(),articles = Article.getTech()));

        return root;
    }

    private void setArticles(int postion) {
        switch (postion){
            case 0:
                articles = Article.getTech();
                break;
            case 1:
                articles = Article.getEd();
                break;
            case 2:
                articles = Article.getDesign();
                break;
            case 3:
                articles = Article.getEnte();
                break;
            case 4:
                articles = Article.getideas();
                break;
            case 5:
                articles = Article.getCul();
                break;
        }
        article_recycler.setAdapter(new ArticleAdapter((AppCompatActivity) getActivity(),articles));
    }

}
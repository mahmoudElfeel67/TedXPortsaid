package com.bloomers.tedxportsaid.Utitltes;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {

    // use your LayoutManager instead
    private final LinearLayoutManager linearLayoutManager;

    protected EndlessOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);


        if (linearLayoutManager.getOrientation()== LinearLayoutManager.HORIZONTAL){
            if (!recyclerView.canScrollHorizontally(1)) {
                onScrolledToEnd();
            }
        }else {
            if (!recyclerView.canScrollVertically(1)) {
                onScrolledToEnd();
            }
        }


    }

    public abstract void onScrolledToEnd();
}
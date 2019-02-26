package com.bloomers.tedxportsaid.Utitltes.other;


import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GridLayoutManagerEXT extends GridLayoutManager {

    public GridLayoutManagerEXT(Context context, int spanCount) {
        super(context, spanCount, android.support.v7.widget.LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (Exception ignored) {

        }
    }
}
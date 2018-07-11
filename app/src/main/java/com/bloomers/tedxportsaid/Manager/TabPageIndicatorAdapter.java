package com.bloomers.tedxportsaid.Manager;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.CustomView.PagerSlidingTabStrip;
import com.bloomers.tedxportsaid.R;

import java.util.ArrayList;
import java.util.Collections;

public class TabPageIndicatorAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {


    private ArrayList<Fragment> fragmentArrayList ;
    private ArrayList<Integer> titles = new ArrayList<>();
    private ArrayList<Integer> drawables = new ArrayList<>();
    private Context context;
    public TabPageIndicatorAdapter(FragmentManager fm,ArrayList<Fragment> fragmentArrayList,Context context) {
        super(fm);
        this.context = context;
        this.fragmentArrayList = fragmentArrayList;

        titles.add(R.string.videos);
        titles.add(R.string.articles);
        titles.add(R.string.speakers);
        titles.add(R.string.schedule);
        titles.add(R.string.about_us);

        drawables.add(R.drawable.videos);
        drawables.add(R.drawable.article);
        drawables.add(R.drawable.speaker);
        drawables.add(R.drawable.schedule);
        drawables.add(R.drawable.about_us);

        if (AppController.getInstance().isArabic(context)){
            Collections.reverse(this.fragmentArrayList);
            Collections.reverse(this.drawables);
            Collections.reverse(this.titles);
        }


    }


    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(titles.get(position));
    }

    @Override
    public int getCount() {
       return drawables.size();
    }


    @Override
    public int getPageIconResId(int position) {
        return drawables.get(position);
    }
}
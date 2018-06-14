package com.bloomers.tedxportsaid.Manager;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bloomers.tedxportsaid.CustomView.PagerSlidingTabStrip;
import com.bloomers.tedxportsaid.R;

import java.util.ArrayList;

public class TabPageIndicatorAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {


    private ArrayList<Fragment> fragmentArrayList ;
    private Context context;
    public TabPageIndicatorAdapter(FragmentManager fm,ArrayList<Fragment> fragmentArrayList,Context context) {
        super(fm);
        this.context = context;
        this.fragmentArrayList = fragmentArrayList;


    }


    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.articles);
            case 1:
                return context.getString(R.string.videos);
            case 2:
                return context.getString(R.string.speakers);
            case 3:
                return  context.getString(R.string.schedule);
            default:
                return  context.getString(R.string.about_us);
        }
    }

    @Override
    public int getCount() {
       return 5;
    }


    @Override
    public int getPageIconResId(int position) {
        switch (position){
            case 0:
                return R.drawable.article;
            case 1:
                return R.drawable.videos;
            case 2:
                return R.drawable.speaker;
            case 3:
                return R.drawable.schedule;
            case 4:
                return R.drawable.about_us;
        }
        return 0;
    }
}
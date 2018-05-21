package com.bloomers.tedxportsaid.Manager;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bloomers.tedxportsaid.CustomView.PagerSlidingTabStrip;
import com.bloomers.tedxportsaid.Fragment.ArticleFragment;
import com.bloomers.tedxportsaid.Fragment.ScheduleFragment;
import com.bloomers.tedxportsaid.Fragment.SpeakerFragment;
import com.bloomers.tedxportsaid.Fragment.VideosFragment;
import com.bloomers.tedxportsaid.R;

public class TabPageIndicatorAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {


    public TabPageIndicatorAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ScheduleFragment.newInstance();
            case 1:
                return VideosFragment.newInstance();
            case 2:
                return SpeakerFragment.newInstance();
            case 3:
                return ScheduleFragment.newInstance();
            case 4:
                return ArticleFragment.newInstance();
        }
        return ArticleFragment.newInstance();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "ar";
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
package com.bloomers.tedxportsaid.CustomView.onboarder;


import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.CustomView.onboarder.utils.ShadowTransformer;

import java.util.ArrayList;
import java.util.List;

public class AhoyOnboarderAdapter extends FragmentStatePagerAdapter implements ShadowTransformer.CardAdapter {

    private final List<AhoyOnboarderFragment> mFragments = new ArrayList<>();
    private final float mBaseElevation;
    private final Typeface typeface;
    private final List<AhoyOnboarderCard> pages;

    AhoyOnboarderAdapter(List<AhoyOnboarderCard> pages, FragmentManager fm, float baseElevation, Typeface typeface) {
        super(fm);
        this.pages = pages;
        this.typeface = typeface;
        this.mBaseElevation = baseElevation;

        for (int i = 0; i < pages.size(); i++) {
            addCardFragment(pages.get(i));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        mFragments.set(position, (AhoyOnboarderFragment) fragment);
        return fragment;
    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        setTypeface(typeface, position);
        return mFragments.get(position).getCardView();
    }

    private void addCardFragment(AhoyOnboarderCard page) {
        mFragments.add(AhoyOnboarderFragment.newInstance(page));
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    private void setTypeface(Typeface typeface, int i) {
        if (typeface != null) {

            if (mFragments.get(i) == null) {
                return;
            }

            if (mFragments.get(i).getTitleView() == null) {
                return;
            }

            if (mFragments.get(i).getTitleView() == null) {
                return;
            }

            mFragments.get(i).getTitleView().setTypeface(typeface);
            mFragments.get(i).getDescriptionView().setTypeface(typeface);

        }
    }

}

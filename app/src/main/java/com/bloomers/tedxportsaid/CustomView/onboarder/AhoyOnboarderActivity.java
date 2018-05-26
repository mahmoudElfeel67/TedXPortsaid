package com.bloomers.tedxportsaid.CustomView.onboarder;


import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaid.CustomView.onboarder.utils.ShadowTransformer;
import com.bloomers.tedxportsaid.CustomView.onboarder.views.CircleIndicatorView;
import com.bloomers.tedxportsaid.CustomView.onboarder.views.FlowingGradientClass;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.AnimationCustomLisntener;
import com.bloomers.tedxportsaid.Utitltes.EndAdLisnter;

import java.util.List;

@SuppressWarnings("unused")
public abstract class AhoyOnboarderActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private CircleIndicatorView circleIndicatorView;
    private ViewPager vpOnboarderPager;
    private AhoyOnboarderAdapter ahoyOnboarderAdapter;
    private TextView btnSkip;
    private ImageView ivNext, ivPrev;
    private FrameLayout navigationControls;
    private RelativeLayout parentLayout;
    private ImageView backgroundImage;

    private Typeface typeface;
    private List<Integer> colorList;
    private boolean solidBackground = false;
    private List<AhoyOnboarderCard> pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahoy);
        setStatusBackgroundColor();
        hideActionBar();

        parentLayout = findViewById(R.id.parent_layout);
        circleIndicatorView = findViewById(R.id.circle_indicator_view);
        btnSkip = findViewById(R.id.btn_skip);
        navigationControls = findViewById(R.id.navigation_layout);
        ivNext = findViewById(R.id.ivNext);
        ivPrev = findViewById(R.id.ivPrev);
        backgroundImage = findViewById(R.id.background_image);
        vpOnboarderPager = findViewById(R.id.vp_pager);
        vpOnboarderPager.addOnPageChangeListener(this);
        btnSkip.setOnClickListener(this);
        ivPrev.setOnClickListener(this);
        ivNext.setOnClickListener(this);

        hideFinish(false);
        fadeOut(ivPrev, false);
    }

    protected void setOnboardPages(List<AhoyOnboarderCard> pages) {

        this.pages = pages;
        ahoyOnboarderAdapter = new AhoyOnboarderAdapter(pages, getSupportFragmentManager(), dpToPixels(0, this), typeface);
        ShadowTransformer mCardShadowTransformer = new ShadowTransformer(vpOnboarderPager, ahoyOnboarderAdapter);
        mCardShadowTransformer.enableScaling();
        vpOnboarderPager.setAdapter(ahoyOnboarderAdapter);
        vpOnboarderPager.setPageTransformer(false, mCardShadowTransformer);
        circleIndicatorView.setPageIndicators(pages.size());

    }

    protected float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    private void setStatusBackgroundColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorAccent));
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        boolean isInFirstPage = vpOnboarderPager.getCurrentItem() == 0;
        boolean isInLastPage = vpOnboarderPager.getCurrentItem() == ahoyOnboarderAdapter.getCount() - 1;

        if (i == R.id.btn_skip && isInLastPage) {
            onFinishButtonPressed();
        } else if (i == R.id.ivPrev && !isInFirstPage) {
            vpOnboarderPager.setCurrentItem(vpOnboarderPager.getCurrentItem() - 1);
        } else if (i == R.id.ivNext && !isInLastPage) {
            vpOnboarderPager.setCurrentItem(vpOnboarderPager.getCurrentItem() + 1);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        int firstPagePosition = 0;
        int lastPagePosition = ahoyOnboarderAdapter.getCount() - 1;
        circleIndicatorView.setCurrentPage(position);
        circleIndicatorView.setCurrentPage(position);

        if (position == lastPagePosition) {
            fadeOut(circleIndicatorView);
            showFinish();
            fadeOut(ivNext);
            fadeIn(ivPrev);
        } else if (position == firstPagePosition) {
            fadeOut(ivPrev);
            fadeIn(ivNext);
            hideFinish();
            fadeIn(circleIndicatorView);
        } else {
            fadeIn(circleIndicatorView);
            hideFinish();
            fadeIn(ivPrev);
            fadeIn(ivNext);
        }

        /*if (solidBackground && (pages.size() == colorList.size())) {
            backgroundImage.setBackgroundColor(ContextCompat.getColor(this, colorList.get(position)));
        }*/

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void fadeOut(View v) {
        fadeOut(v, true);
    }

    private void fadeOut(final View v, boolean delay) {

        long duration = 0;
        if (delay) {
            duration = 300;
        }

        if (v.getVisibility() != View.GONE) {
            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
            fadeOut.setDuration(duration);
            fadeOut.setAnimationListener(new AnimationCustomLisntener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    v.setVisibility(View.GONE);
                }
            });
            v.startAnimation(fadeOut);
        }
    }

    private void fadeIn(final View v) {

        if (v.getVisibility() != View.VISIBLE) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
            fadeIn.setDuration(300);
            fadeIn.setAnimationListener(new AnimationCustomLisntener() {
                @Override
                public void onAnimationEnd(Animation animation) {

                    v.setVisibility(View.VISIBLE);
                }
            });
            v.startAnimation(fadeIn);
        }
    }

    private void showFinish() {
        btnSkip.setVisibility(View.VISIBLE);
        this.btnSkip.animate().translationY(0 - dpToPixels(5, this)).setInterpolator(new DecelerateInterpolator()).setDuration(500).start();
    }

    private void hideFinish(boolean delay) {

        long duration = 0;
        if (delay) {
            duration = 250;
        }

        this.btnSkip.animate().translationY(this.btnSkip.getBottom() + dpToPixels(100, this)).setInterpolator(new AccelerateInterpolator()).setDuration(duration).setListener(new EndAdLisnter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                btnSkip.setVisibility(View.VISIBLE);
            }
        }).start();
    }

    private void hideFinish() {
        hideFinish(true);
    }

    private void hideActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    protected abstract void onFinishButtonPressed();

    protected void showNavigationControls(@SuppressWarnings("SameParameterValue") boolean navigation) {
        if (navigation) {
            navigationControls.setVisibility(View.VISIBLE);
        } else {
            navigationControls.setVisibility(View.GONE);
        }
    }

    public void setImageBackground(int resId) {
        backgroundImage.setImageResource(resId);
    }

    public void setColorBackground(@ColorRes int color) {
        backgroundImage.setBackgroundColor(ContextCompat.getColor(this, color));
    }

    public void setColorBackground(List<Integer> color) {
        this.colorList = color;
        solidBackground = true;
        backgroundImage.setBackgroundColor(ContextCompat.getColor(this, color.get(0)));
    }

    public void setGradientBackground(int drawable) {
        FlowingGradientClass grad = new FlowingGradientClass();
        grad.setBackgroundResource(drawable).onRelativeLayout(parentLayout).setTransitionDuration(4000).start();
    }

    public void setInactiveIndicatorColor(int color) {
        this.circleIndicatorView.setInactiveIndicatorColor(color);
    }

    public void setActiveIndicatorColor(int color) {
        this.circleIndicatorView.setActiveIndicatorColor(color);
    }

    /**
     * <br/><br/>
     * <b>N.B. Builds before JELLY_BEAN will use the default style</b>
     * <br/><br/>
     * Set the xml drawable style of the skip/done button, <br/>
     * using for example: ContextCompat.getDrawable(this, R.drawable.rectangle_button);
     *
     * @param res A drawable xml file representing your desired button style
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void setFinishButtonDrawableStyle(Drawable res) {
        btnSkip.setBackground(res);
    }

    protected void setFinishButtonTitle(@SuppressWarnings("SameParameterValue") CharSequence title) {
        btnSkip.setText(title);
    }

    public void setFinishButtonTitle(@StringRes int titleResId) {
        btnSkip.setText(titleResId);
    }

    protected void setFont(Typeface typeface) {
        this.btnSkip.setTypeface(typeface);
        this.typeface = typeface;
    }

}

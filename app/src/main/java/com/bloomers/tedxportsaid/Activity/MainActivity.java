package com.bloomers.tedxportsaid.Activity;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.CustomView.PagerSlidingTabStrip;
import com.bloomers.tedxportsaid.Dialog.AskSpeakerDialog;
import com.bloomers.tedxportsaid.Fragment.TeamFragment;
import com.bloomers.tedxportsaid.Manager.TabPageIndicatorAdapter;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.ints;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.bloomers.tedxportsaid.Utitltes.other.delay;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scale_view) ImageView scale_view;
    @BindView(R.id.team_bg) ImageView team_bg;
    @BindView(R.id.team_image) ImageView team_image;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.team_info) TextView team_info;
    @BindView(R.id.display_container) RelativeLayout display_container;
    int[] originalPos;
    int current_postion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ViewPager pager = findViewById(R.id.pager);
        PagerSlidingTabStrip tab = findViewById(R.id.indicator);
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager(), new TeamFragment.onCLick() {
            @Override
            public void onClick(View view) {
                callTeamPreview(view);
            }
        });

        tab.setOnItemSelected(new PagerSlidingTabStrip.OnItemSelected() {
            @Override
            public void onSelectPostion(int postion) {
                current_postion = postion;
                switch (postion) {
                    case 0:
                        fab.setImageResource(R.drawable.question_mark);
                        break;
                    case 1:
                        fab.setImageResource(R.drawable.shuffle);
                        break;
                    case 2:
                        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
                        fab.setImageResource(R.drawable.ask);
                        break;
                    case 3:
                        fab.animate().scaleX(0).scaleY(0).setInterpolator(new FastOutSlowInInterpolator()).start();
                        break;
                    case 4:
                        break;
                }

            }
        });
        pager.setAdapter(adapter);
        tab.setViewPager(pager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (current_postion) {
                    case 2:
                        HeavilyUsed.callSaveDialog(MainActivity.this, new AskSpeakerDialog(), null);
                        break;
                   default:
                        showCusomtToast(MainActivity.this,"مفيش كود يعمل حاجه هنا لسه !!",null,false);
                        break;
                }
            }
        });

    }

    private void callTeamPreview(View view) {
        scale_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        originalPos = new int[ 2 ];
        view.getLocationInWindow(originalPos);
        int x = originalPos[ 0 ];
        int y = originalPos[ 1 ] - ints.dp2px(23, MainActivity.this);
        originalPos[ 1 ] = y;
        scale_view.setAlpha(1F);
        scale_view.setVisibility(View.VISIBLE);
        scale_view.setScaleX(0);
        scale_view.setScaleY(0);
        scale_view.setX(x);
        scale_view.setY(y);

        display_container.setPivotX(0);
        display_container.setPivotY(0);

        display_container.setX(x);
        display_container.setY(y);

        display_container.setScaleX(.20F);
        display_container.setScaleY(.20F);

        display_container.requestLayout();
        display_container.setPivotX(0.50F);
        display_container.setPivotY(0.50F);

        int animation_period = 1100;

        GlideApp.with(MainActivity.this).load(R.drawable.portofilio).override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(team_image);

        scale_view.animate().setStartDelay(0).scaleX(14).scaleY(14).setDuration(animation_period - 100).start();

        display_container.animate().x(0).y(0).scaleY(1).scaleX(1).setDuration(animation_period).alpha(1).start();
        team_bg.animate().setStartDelay(500).alpha(0.70F).start();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (originalPos != null) {
            dissmisTeamMember();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(getString(R.string.app_name), bm, AppController.easyColor(MainActivity.this, R.color.white));
            setTaskDescription(taskDesc);
        }

    }

    private void dissmisTeamMember() {
        int animation_period = 1000;
        display_container.setPivotX(0);
        display_container.setPivotY(0);
        scale_view.animate().x(originalPos[ 0 ]).y(originalPos[ 1 ]).scaleX(0).scaleY(0).setDuration(animation_period - 100).start();
        scale_view.animate().alpha(0).setStartDelay(500).setDuration(500).start();
        display_container.animate().x(originalPos[ 0 ] + ints.px2dp(80, MainActivity.this)).y(originalPos[ 1 ] + ints.px2dp(80, MainActivity.this)).scaleX(.20F).scaleY(.20F).alpha(0).setDuration(animation_period - 200).start();
        team_bg.animate().alpha(0).start();
        originalPos = null;
    }

    public static void showCusomtToast(final Activity activity, String text, ViewGroup viewGroup, boolean success) {
        final ViewGroup rootLayout;

        if (viewGroup == null) {
            rootLayout = activity.findViewById(android.R.id.content);
        } else {
            rootLayout = viewGroup;
        }

        final View view = View.inflate(activity, R.layout.toast_view, null);
        TextView messageText = view.findViewById(R.id.message);
        final CardView cardView = view.findViewById(R.id.card);
        if (!success) {
            cardView.setCardBackgroundColor(AppController.easyColor(activity, R.color.colorAccent));
            ((ImageView) view.findViewById(R.id.exes)).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            messageText.setTextColor(Color.WHITE);
        }

        messageText.setText(text);
        rootLayout.addView(view);

        cardView.animate().translationY(0).start();

        new delay(cardView, 2000) {
            @Override
            protected void OnDelayEnded() {
                cardView.animate().translationY(ints.dp2px(110, activity)).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        rootLayout.removeView(view);
                    }
                }).start();
            }
        };

    }

}

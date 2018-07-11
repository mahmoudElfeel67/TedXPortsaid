package com.bloomers.tedxportsaid.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.CustomView.CountDownView;
import com.bloomers.tedxportsaid.CustomView.PagerSlidingTabStrip;
import com.bloomers.tedxportsaid.Dialog.AskSpeakerDialog;
import com.bloomers.tedxportsaid.Dialog.RandomDialog;
import com.bloomers.tedxportsaid.Fragment.AboutUsFragment;
import com.bloomers.tedxportsaid.Fragment.ArticleFragment;
import com.bloomers.tedxportsaid.Fragment.ScheduleFragment;
import com.bloomers.tedxportsaid.Fragment.SpeakerFragment;
import com.bloomers.tedxportsaid.Fragment.TeamFragment;
import com.bloomers.tedxportsaid.Fragment.VideosFragment;
import com.bloomers.tedxportsaid.Manager.TabPageIndicatorAdapter;
import com.bloomers.tedxportsaid.Model.TeamMember;
import com.bloomers.tedxportsaid.Model.event_date;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.ints;
import com.bloomers.tedxportsaid.Utitltes.other.GlideApp;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.bloomers.tedxportsaid.Utitltes.other.delay;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scale_view) ImageView scale_view;
    @BindView(R.id.team_bg) ImageView team_bg;
    @BindView(R.id.team_image) ImageView team_image;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.team_info) TextView team_info;
    @BindView(R.id.display_container) RelativeLayout display_container;
    @BindView(R.id.count_down) CountDownView countDownView;
    int[] originalPos;
    ViewPager pager;
    int current_position = 0;
    event_date eventDate;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pager = findViewById(R.id.pager);
        PagerSlidingTabStrip tab = findViewById(R.id.indicator);
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(VideosFragment.newInstance());
        fragmentArrayList.add(ArticleFragment.newInstance());
        fragmentArrayList.add(SpeakerFragment.newInstance());
        fragmentArrayList.add(ScheduleFragment.newInstance());
        fragmentArrayList.add(AboutUsFragment.newInstance(new TeamFragment.onCLick() {
            @Override
            public void onClick(View view, TeamMember teamMember) {
                callTeamPreview(view,teamMember,1100);
            }
        }));
        FragmentPagerAdapter adapter = new TabPageIndicatorAdapter(getSupportFragmentManager(), fragmentArrayList, MainActivity.this);
        tab.setOnItemSelected(new PagerSlidingTabStrip.OnItemSelected() {
            @Override
            public void onSelectPostion(int postion) {
                current_position = postion;
                if (AppController.getInstance().isArabic(MainActivity.this)){
                    switch (postion) {
                        case 0:
                            current_position = 4;
                            break;
                        case 1:
                            current_position = 3;
                            break;
                        case 2:
                            current_position= 2;
                            break;
                        case 3:
                            current_position= 1;
                            break;
                        case 4:
                            current_position= 0;
                            break;
                    }
                }
                switch (current_position) {
                    case 0:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.question_mark);
                        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
                        break;
                    case 1:
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.shuffle);
                        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
                        break;
                    case 2:
                        fab.setVisibility(View.VISIBLE);
                        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
                        fab.setImageResource(R.drawable.ask);
                        break;
                    case 3:
                        fab.animate().scaleX(0).scaleY(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                fab.setVisibility(View.GONE);
                            }
                        }).setInterpolator(new FastOutSlowInInterpolator()).start();
                        break;
                    case 4:
                        fab.animate().scaleX(0).scaleY(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                fab.setVisibility(View.GONE);
                            }
                        }).setInterpolator(new FastOutSlowInInterpolator()).start();
                        break;
                }

            }
        });

        pager.setAdapter(adapter);
        if (AppController.getInstance().isArabic(this)){
            pager.setCurrentItem(fragmentArrayList.size()-1);
        }
        tab.setViewPager(pager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (current_position) {
                    case 0:
                        if (ArticleFragment.articles != null && ArticleFragment.articles.size() != 0) {
                            HeavilyUsed.callSaveDialog(MainActivity.this, RandomDialog.newInstance(true, ArticleFragment.articles), null);
                        } else {
                            MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_articles_randomize), null, false);
                        }

                        break;
                    case 1:
                        if (VideosFragment.videos != null && VideosFragment.videos.size() != 0) {
                            HeavilyUsed.callSaveDialog(MainActivity.this, RandomDialog.newInstance(false, VideosFragment.videos), null);
                        } else {
                            MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_videos_randomize), null, false);
                        }
                        break;
                    case 2:
                        if (SpeakerFragment.speakers != null && SpeakerFragment.speakers.size() != 0) {
                            HeavilyUsed.callSaveDialog(MainActivity.this, AskSpeakerDialog.newInstance(SpeakerFragment.speakers), null);
                        } else {
                            MainActivity.showCustomToast(MainActivity.this, getString(R.string.no_speakers_randomize), null, false);
                        }


                        break;
                }
            }
        });

        FirebaseDatabase.getInstance().getReference().child("event_date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null&&dataSnapshot.exists()){
                    eventDate = dataSnapshot.getValue(event_date.class);


                    Calendar c2 = Calendar.getInstance();
                    c2.set(eventDate.getBefore_year(), eventDate.getBefore_month()- 1, eventDate.getBefore_day(),0,0,0);
                    if (System.currentTimeMillis() > c2.getTimeInMillis()){
                        countDownView.setStartDuration(eventTime(eventDate));
                        countDownView.start();
                        countDownView.setVisibility(View.VISIBLE);


                        countDownView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                countDownView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                                Shader textShader = new LinearGradient(0, 0, countDownView.getWidth(), countDownView.getHeight(),
                                        new int[]{getResources().getColor(R.color.gradient_startColor),
                                                getResources().getColor(R.color.gradient_endColor)},
                                        new float[]{0.50F, 1}, Shader.TileMode.CLAMP);
                                countDownView.getTextPaint().setShader(textShader);
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        display_container.setPivotX(0);
        display_container.setPivotY(0);
        scale_view.animate().x(5).y(5).scaleX(0).scaleY(0).setDuration(0).start();
        scale_view.animate().alpha(0).setStartDelay(500).setDuration(500).start();
        display_container.animate().x(5).y(5).scaleX(.20F).scaleY(.20F).alpha(0).setDuration(0).start();

        if (AppController.getInstance().isArabic(this)){
            fab.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    fab.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    callTeamPreview(countDownView,new TeamMember(),200);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dissmisTeamMember(300);
                        }
                    },200);
                }
            });
        }


    }

    public long eventTime(event_date eventDate) {
        Calendar c2 = Calendar.getInstance();
        c2.set(eventDate.getYear(), eventDate.getMonth()- 1, eventDate.getDay(), eventDate.getHour(), eventDate.getMinute(), 0);
        if (System.currentTimeMillis() > c2.getTimeInMillis()) {
            countDownView.setVisibility(View.GONE);
        }
        return c2.getTimeInMillis() - System.currentTimeMillis();
    }


    @OnClick(R.id.count_down)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.count_down:
                String day;
                switch (countDownView.getDay(eventTime(eventDate))) {
                    case 1:
                        day = " يوم ";
                        break;
                    case 2:
                        day = " يومين ";
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        day = countDownView.getDay(eventTime(eventDate)) + " أيام ";
                        break;
                    default:
                        day = countDownView.getDay(eventTime(eventDate)) + " يوم ";
                        break;
                }

                String hour;
                switch (countDownView.getHour(eventTime(eventDate))) {
                    case 1:
                        hour = " ساعه ";
                        break;
                    case 2:
                        hour = " ساعتين ";
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        hour = countDownView.getHour(eventTime(eventDate)) + " ساعات ";
                        break;
                    default:
                        hour = countDownView.getHour(eventTime(eventDate)) + " ساعه ";
                        break;
                }

                MainActivity.showCustomToast(MainActivity.this, "لسه " + day + " و " + hour + "مستعديين !!", null, true, 5000);
                if (AppController.getInstance().isArabic(this)){
                    pager.setCurrentItem(1,true);
                }else{
                    pager.setCurrentItem(3,true);
                }
                break;
        }

    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void callTeamPreview(View view, TeamMember teamMember,int duration) {
        fab.animate().scaleX(0).scaleY(0).setInterpolator(new FastOutSlowInInterpolator()).start();
        TextView team_member_name = findViewById(R.id.team_member_name);
        TextView team_info = findViewById(R.id.team_info);
        team_member_name.setText(teamMember.getName() + " - "+teamMember.getTeam());
        team_info.setText(teamMember.getDesc());
        scale_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        originalPos = new int[2];
        view.getLocationInWindow(originalPos);
        int x = originalPos[0];
        int y = originalPos[1] - ints.dp2px(23, MainActivity.this);

        originalPos[1] = y;
        scale_view.setAlpha(1F);
        scale_view.setVisibility(View.VISIBLE);
        scale_view.setScaleX(.20F);
        scale_view.setScaleY(.20F);
        scale_view.setX(x);
        scale_view.setY(y);


        display_container.setPivotX(0);
        display_container.setPivotY(0);

        display_container.setX(x);
        display_container.setY(y);

        display_container.setScaleX(.20F);
        display_container.setScaleY(.20F);

        display_container.requestLayout();


        GlideApp.with(MainActivity.this)
                .load(teamMember.getProfile_url())
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(team_image);


        if (duration==200){
            display_container.setVisibility(View.INVISIBLE);
            scale_view.setVisibility(View.INVISIBLE);
            team_bg.setVisibility(View.INVISIBLE);

            scale_view.animate().setStartDelay(0).scaleX(16).scaleY(16).setDuration(duration - 100).start();

            display_container.animate().x(0).y(0).scaleY(1).scaleX(1).setDuration(duration).start();
            team_bg.animate().setStartDelay(500).start();


        }else {
            display_container.setVisibility(View.VISIBLE);
            scale_view.setVisibility(View.VISIBLE);
            team_bg.setVisibility(View.VISIBLE);
            scale_view.animate().setStartDelay(0).scaleX(16).scaleY(16).setDuration(duration - 100).start();

            display_container.animate().x(0).y(0).scaleY(1).scaleX(1).setDuration(duration).alpha(1).start();
            team_bg.animate().setStartDelay(500).alpha(0.50F).start();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onDestroy() {
        if (countDownView != null) {
            countDownView.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (originalPos != null) {
            dissmisTeamMember(1000);
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

    private void dissmisTeamMember(int duration) {
        fab.animate().scaleX(1).scaleY(1).setInterpolator(new FastOutSlowInInterpolator()).start();
        display_container.setPivotX(0);
        display_container.setPivotY(0);
        scale_view.animate().x(originalPos[0]).y(originalPos[1]).scaleX(0).scaleY(0).setDuration(duration - 100).start();
        scale_view.animate().alpha(0).setStartDelay(500).setDuration(500).start();
        display_container.animate().x(originalPos[0] + ints.px2dp(80, MainActivity.this)).y(originalPos[1] + ints.px2dp(80, MainActivity.this)).scaleX(.20F).scaleY(.20F).alpha(0).setDuration(duration - 200).start();
        team_bg.animate().alpha(0).start();
        originalPos = null;
    }

    public static void showCustomToast(final Activity activity, String text, ViewGroup viewGroup, boolean success) {
        showCustomToast(activity, text, viewGroup, success, 2000);
    }

    public static void showCustomToast(final Activity activity, String text, ViewGroup viewGroup, boolean success, int period) {
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

        new delay(cardView, period) {
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

package com.bloomers.tedxportsaidadmin.Activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bloomers.tedxportsaidadmin.AppController;
import com.bloomers.tedxportsaidadmin.R;
import com.bloomers.tedxportsaidadmin.Utitltes.ints;
import com.bloomers.tedxportsaidadmin.Utitltes.other.delay;
import com.bloomers.tedxportsaidadmin.fragments.SpeakerFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppController.getInstance().FragmentEaseNoAnimNoback(this)
                .replace(R.id.frame,SpeakerFragment.newInstance())
                .commitAllowingStateLoss();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public static void showCustomToast(final Activity activity, String text, ViewGroup viewGroup, boolean success) {
        showCustomToast(activity, text, viewGroup, success, 2000);
    }

    private static void showCustomToast(final Activity activity, String text, ViewGroup viewGroup, boolean success, int period) {
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

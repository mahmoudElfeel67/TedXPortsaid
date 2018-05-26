package com.bloomers.tedxportsaid.CustomView.onboarder.views;


import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressWarnings("unused")
public class FlowingGradientClass {

    private int Duration = 4000;
    private RelativeLayout rl;
    private LinearLayout ll;
    private ImageView im;
    private TextView textView;
    private int d;
    private AnimationDrawable frameAnimation;

    public FlowingGradientClass setTransitionDuration(@SuppressWarnings("SameParameterValue") int time) {
        this.Duration = time;

        return this;
    }

    public FlowingGradientClass onLinearLayout(LinearLayout ll) {
        this.ll = ll;
        return this;
    }

    public FlowingGradientClass onImageView(ImageView im) {
        this.im = im;
        return this;
    }

    public FlowingGradientClass onRelativeLayout(RelativeLayout rl) {
        this.rl = rl;
        return this;
    }

    public FlowingGradientClass onTextView(TextView textView) {
        this.textView = textView;
        return this;
    }

    public void start() {

        if (ll != null) {
            ll.setBackgroundResource(d);
        } else if (rl != null) {
            rl.setBackgroundResource(d);
        } else if (im != null) {
            im.setBackgroundResource(d);
        } else if (textView != null) {
            textView.setBackgroundResource(d);
        }
        if (ll != null) {
            frameAnimation = (AnimationDrawable) ll.getBackground();
        } else if (rl != null) {
            frameAnimation = (AnimationDrawable) rl.getBackground();
        } else if (im != null) {
            frameAnimation = (AnimationDrawable) im.getBackground();
        } else if (textView != null) {
            frameAnimation = (AnimationDrawable) textView.getBackground();
        }
        frameAnimation.setEnterFadeDuration(Duration);
        frameAnimation.setExitFadeDuration(Duration);
        frameAnimation.start();

    }

    public FlowingGradientClass setBackgroundResource(int d) {
        this.d = d;
        return this;
    }

    public FlowingGradientClass setAlpha(int alphaint) {
        frameAnimation.setAlpha(alphaint);
        return this;
    }

}
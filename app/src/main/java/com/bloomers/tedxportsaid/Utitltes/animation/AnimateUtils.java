package com.bloomers.tedxportsaid.Utitltes.animation;


import android.app.Activity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;

import com.bloomers.tedxportsaid.Utitltes.Listners.AnimationCustomLisntener;

@SuppressWarnings("unused")
public class AnimateUtils {

    public static void ScaleInAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean isInvisible) {
        ScaleAnimation scaleInAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleInAnimation.setInterpolator(interpolator);
        scaleInAnimation.setDuration(duration);
        scaleInAnimation.setStartOffset(startOffset);
        scaleInAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (isInvisible) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        view.startAnimation(scaleInAnimation);
    }

    public static void ScaleInAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        ScaleAnimation scaleInAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleInAnimation.setInterpolator(interpolator);
        scaleInAnimation.setDuration(duration);
        scaleInAnimation.setStartOffset(startOffset);
        if (animationListener != null) scaleInAnimation.setAnimationListener(animationListener);
        view.startAnimation(scaleInAnimation);
    }

    public static void ScaleOutAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean invisibleAtEnd) {
        ScaleAnimation scaleInAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleInAnimation.setInterpolator(interpolator);
        scaleInAnimation.setDuration(duration);
        scaleInAnimation.setStartOffset(startOffset);
        scaleInAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (invisibleAtEnd) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        });
        view.startAnimation(scaleInAnimation);
    }

    public static void ScaleOutAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        ScaleAnimation scaleInAnimation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleInAnimation.setInterpolator(interpolator);
        scaleInAnimation.setDuration(duration);
        scaleInAnimation.setStartOffset(startOffset);
        if (animationListener != null) scaleInAnimation.setAnimationListener(animationListener);
        view.startAnimation(scaleInAnimation);
    }

    public static void FlipOutHorizontalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean invisibleAtEnd) {
        ScaleAnimation flipOutAnimation = new ScaleAnimation(1f, 0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipOutAnimation.setInterpolator(interpolator);
        flipOutAnimation.setDuration(duration);
        flipOutAnimation.setStartOffset(startOffset);
        flipOutAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (invisibleAtEnd) {
                    view.setVisibility(View.INVISIBLE);
                }
            }

        });
        view.startAnimation(flipOutAnimation);
    }

    public static void FlipOutHorizontalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        ScaleAnimation flipOutAnimation = new ScaleAnimation(1f, 0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipOutAnimation.setInterpolator(interpolator);
        flipOutAnimation.setDuration(duration);
        flipOutAnimation.setStartOffset(startOffset);
        if (animationListener != null) flipOutAnimation.setAnimationListener(animationListener);
        view.startAnimation(flipOutAnimation);
    }

    public static void FlipInHorizontalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean visibleAtEnd) {
        ScaleAnimation flipInAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipInAnimation.setInterpolator(interpolator);
        flipInAnimation.setDuration(duration);
        flipInAnimation.setStartOffset(startOffset);
        flipInAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (visibleAtEnd) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        view.startAnimation(flipInAnimation);
    }

    public static void FlipInHorizontalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        ScaleAnimation flipInAnimation = new ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipInAnimation.setInterpolator(interpolator);
        flipInAnimation.setDuration(duration);
        flipInAnimation.setStartOffset(startOffset);
        if (animationListener != null) flipInAnimation.setAnimationListener(animationListener);
        view.startAnimation(flipInAnimation);
    }

    public static void FlipOutVerticalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean invisibleAtEnd) {
        ScaleAnimation flipOutAnimation = new ScaleAnimation(1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipOutAnimation.setInterpolator(interpolator);
        flipOutAnimation.setDuration(duration);
        flipOutAnimation.setStartOffset(startOffset);
        flipOutAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (invisibleAtEnd) {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        });
        view.startAnimation(flipOutAnimation);
    }

    public static void FlipOutVerticalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        ScaleAnimation flipOutAnimation = new ScaleAnimation(1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipOutAnimation.setInterpolator(interpolator);
        flipOutAnimation.setDuration(duration);
        flipOutAnimation.setStartOffset(startOffset);
        if (animationListener != null) flipOutAnimation.setAnimationListener(animationListener);
        view.startAnimation(flipOutAnimation);
    }

    public static void FlipInVerticalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean visibleAtEnd) {
        ScaleAnimation flipOutAnimation = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipOutAnimation.setInterpolator(interpolator);
        flipOutAnimation.setDuration(duration);
        flipOutAnimation.setStartOffset(startOffset);
        flipOutAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (visibleAtEnd) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        view.startAnimation(flipOutAnimation);
    }

    public static void FlipInVerticalAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        ScaleAnimation flipOutAnimation = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        flipOutAnimation.setInterpolator(interpolator);
        flipOutAnimation.setDuration(duration);
        flipOutAnimation.setStartOffset(startOffset);
        if (animationListener != null) flipOutAnimation.setAnimationListener(animationListener);
        view.startAnimation(flipOutAnimation);
    }

    public static void FadeInAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean visibleAtEnd) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setInterpolator(interpolator);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(startOffset);
        alphaAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (visibleAtEnd) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        });
        view.startAnimation(alphaAnimation);
    }

    public static void FadeInAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setInterpolator(interpolator);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(startOffset);
        if (animationListener != null) alphaAnimation.setAnimationListener(animationListener);
        view.startAnimation(alphaAnimation);
    }

    public static void FadeOutAnimation(final View view, int startOffset, int duration, Interpolator interpolator, final boolean invisibleAtEnd) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setInterpolator(interpolator);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(startOffset);
        alphaAnimation.setAnimationListener(new AnimationCustomLisntener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                if (invisibleAtEnd) {
                    view.setVisibility(View.INVISIBLE);
                }
            }

        });
        view.startAnimation(alphaAnimation);
    }

    public static void FadeOutAnimation(final View view, int startOffset, int duration, Interpolator interpolator, AnimationCustomLisntener animationListener) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setInterpolator(interpolator);
        alphaAnimation.setDuration(duration);
        alphaAnimation.setStartOffset(startOffset);
        if (animationListener != null) alphaAnimation.setAnimationListener(animationListener);
        view.startAnimation(alphaAnimation);
    }

    public static void flipTwoViews(Activity activity, int parent, int face, int back) {
        View rootLayout = activity.findViewById(parent);
        View cardFace = activity.findViewById(face);
        View cardBack = activity.findViewById(back);

        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);

        if (cardFace.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }

    public static void resizeLay(View resizedLay, int fromWidth, int fromHeight, int toWidth, int toHeight) {
        if (resizedLay != null) {
            ResizeAnimations resizeAnimation = new ResizeAnimations(resizedLay, fromWidth, fromHeight, toWidth, toHeight);
            resizeAnimation.setDuration(400);
            resizedLay.startAnimation(resizeAnimation);
            resizedLay.requestLayout();
            resizedLay.invalidate();
        }
    }

    public static void resizeLayMinimize(View resizedLay, int fromWidth, int fromHeight) {
        if (resizedLay != null) {
            ResizeAnimations resizeAnimation = new ResizeAnimations(resizedLay, fromWidth, fromHeight, 0, 0);
            resizeAnimation.setDuration(400);
            resizedLay.startAnimation(resizeAnimation);
            resizedLay.requestLayout();
            resizedLay.invalidate();
        }
    }

    public static void resizeLayMaximize(View resizedLay, int toWidth, int toHeight) {
        if (resizedLay != null) {
            ResizeAnimations resizeAnimation = new ResizeAnimations(resizedLay, 0, 0, toWidth, toHeight);
            resizeAnimation.setDuration(400);
            resizedLay.startAnimation(resizeAnimation);
            resizedLay.requestLayout();
            resizedLay.invalidate();
        }
    }

    public static void animateInOut(final View view) {
        view.animate().scaleX(0.7F).setDuration(100).start();
        view.animate().scaleY(0.7F).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.animate().scaleX(1F).setDuration(100).start();
                view.animate().scaleY(1F).setDuration(100).start();
            }
        }).setDuration(100).start();
    }
}

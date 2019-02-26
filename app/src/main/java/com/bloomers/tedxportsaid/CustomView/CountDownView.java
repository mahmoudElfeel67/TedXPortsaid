package com.bloomers.tedxportsaid.CustomView;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;

import java.util.Locale;
public class CountDownView extends View {

    private static final int HOUR = 3600000;
    private static final int MIN = 60000;
    private static final int SEC = 1000;
    private final TextPaint textPaint = new TextPaint(1);
    @Nullable
    private Layout textLayout;
    private final SpannableStringBuilder spannableString = new SpannableStringBuilder();
    private CountDownTimer timer;
    private long startDuration;
    private long currentDuration;
    private boolean timerRunning;

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        this.textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), AppController.bigFont));
        TypedArray ta = this.getContext().obtainStyledAttributes(attrs, R.styleable.CountDownView);
        int startDuration = ta.getInt(R.styleable.CountDownView_startDuration, 0);
        int textSize = ta.getDimensionPixelSize(R.styleable.CountDownView_android_textSize, (int) dpToPx(this.getResources()));
        ta.recycle();
        this.textPaint.setTextSize((float) textSize);

        this.setStartDuration((long) startDuration);
    }

    public void setStartDuration(long duration) {
        if (!this.timerRunning) {
            this.startDuration = this.currentDuration = duration;
            this.updateText(duration);
        }
    }

    public void start() {
        if (!this.timerRunning) {
            this.timerRunning = true;
            this.timer = new CountDownTimer(this.currentDuration, 1000L) {
                public void onTick(long millis) {
                    currentDuration = millis;
                    updateText(millis);
                    invalidate();
                }

                public void onFinish() {
                    stop();
                }
            };
            this.timer.start();
        }
    }

    public void reset() {
        this.stop();
        this.setStartDuration(this.startDuration);
        this.invalidate();
    }

    public void stop() {
        if (this.timerRunning) {
            this.timerRunning = false;
            this.timer.cancel();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.textLayout == null) {
            this.updateText(this.currentDuration);
        }

        this.setMeasuredDimension(this.textLayout.getWidth(), this.textLayout.getHeight());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.textLayout.draw(canvas);
    }

    @Nullable
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        CountDownViewState viewState = new CountDownViewState(superState);
        viewState.startDuration = this.startDuration;
        viewState.currentDuration = this.currentDuration;
        viewState.timerRunning = this.timerRunning;
        return viewState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        CountDownViewState viewState = (CountDownViewState) state;
        super.onRestoreInstanceState(viewState.getSuperState());
        this.setStartDuration(viewState.startDuration);
        this.currentDuration = viewState.currentDuration;
        if (viewState.timerRunning) {
            this.start();
        }

    }

    private void updateText(long duration) {
        String text = generateCountdownText(duration,getContext());
        this.textLayout = this.createTextLayout(text);
    }


    public TextPaint getTextPaint() {
        return textPaint;
    }

    private Layout createTextLayout(String text) {
        int textWidth = (int) this.textPaint.measureText(text);
        int unitTextSize = (int) (this.textPaint.getTextSize() / 2.0F);
        this.spannableString.clear();
        this.spannableString.clearSpans();
        this.spannableString.append(text);
        this.spannableString.setSpan(new ForegroundColorSpan(AppController.easyColor(getContext(), R.color.red_color)), 0, text.length(), 33);

        int daIndex = text.indexOf(getContext().getString(R.string.day_ext));
        int hrIndex = text.indexOf(getContext().getString(R.string.hour_ext));
        int minIndex = text.indexOf(getContext().getString(R.string.minute_ext));
        int secIndex = text.indexOf(getContext().getString(R.string.second_ext));
        this.spannableString.setSpan(new AbsoluteSizeSpan(unitTextSize), daIndex, daIndex + 1, 33);
        this.spannableString.setSpan(new AbsoluteSizeSpan(unitTextSize), hrIndex, hrIndex + 1, 33);
        this.spannableString.setSpan(new AbsoluteSizeSpan(unitTextSize), minIndex, minIndex + 1, 33);
        this.spannableString.setSpan(new AbsoluteSizeSpan(unitTextSize), secIndex, secIndex + 1, 33);
        return new StaticLayout(this.spannableString, this.textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 0.0F, 0.0F, true);
    }

     private String generateCountdownText(long duration, Context activity) {
        int day = (int) (duration / 86400000);
        int calhr = (int) (duration / HOUR);
        int hr = (int) ((duration - (long) (day * 86400000)) / 3600000L);
        int min = (int) ((duration - (long) (calhr * 3600000)) / 60000L);
        int sec = (int) ((duration - (long) (calhr * 3600000) - (long) (min * '\uea60')) / 1000L);
        Locale locale = Locale.getDefault();
        String format = "%02d";
        String formattedDay = String.format(locale, format, day);
        String formattedHr = String.format(locale, format, hr);
        String formattedMin = String.format(locale, format, min);
        String formattedSec = String.format(locale, format, sec);
        return String.format(locale, activity.getString(R.string.extenstion), formattedDay, formattedHr, formattedMin, formattedSec);
    }

    public int getDay(long duration) {
        return (int) (duration / 86400000);
    }

    public int getHour(long duration) {
        int day = (int) (duration / 86400000);
        return (int) ((duration - (long) (day * 86400000)) / 3600000L);
    }

    private static float dpToPx(Resources resources) {
        return TypedValue.applyDimension(1, (float) 18, resources.getDisplayMetrics());
    }
}

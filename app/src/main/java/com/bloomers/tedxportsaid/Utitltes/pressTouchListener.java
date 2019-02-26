package com.bloomers.tedxportsaid.Utitltes;


import android.view.MotionEvent;
import android.view.View;

public abstract class pressTouchListener implements View.OnTouchListener {

    protected pressTouchListener() {
    }

    protected pressTouchListener(View view) {
        view.setOnTouchListener(this);
    }

    protected abstract void OnClick(View view);

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                animate(view, 0.97F, 200);
                break;
            case MotionEvent.ACTION_UP:
                animate(view, 1F, 250);
                OnClick(view);
                break;
            case MotionEvent.ACTION_CANCEL:
                animate(view, 1F, 250);
                break;
        }
        return true;
    }

    private void animate(View view, float scale, int duration) {
        view.animate().scaleX(scale).setDuration(duration).start();
        view.animate().scaleY(scale).setDuration(duration).start();
    }

}

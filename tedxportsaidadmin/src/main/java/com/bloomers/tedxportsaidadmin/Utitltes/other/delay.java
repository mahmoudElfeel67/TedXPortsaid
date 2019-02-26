package com.bloomers.tedxportsaidadmin.Utitltes.other;


import android.os.Handler;
import android.view.View;

import java.lang.ref.WeakReference;

public abstract class delay {

    private WeakReference<View> viewWeakReference;

    protected delay(final View view, int Amount) {
        if (view != null) {
            viewWeakReference = new WeakReference<>(view);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (viewWeakReference != null && viewWeakReference.get() != null) {
                        OnDelayEnded();
                    }
                }
            }, Amount);
        }

    }

    protected abstract void OnDelayEnded();
}

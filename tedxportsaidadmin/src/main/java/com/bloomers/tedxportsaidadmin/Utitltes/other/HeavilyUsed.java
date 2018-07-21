package com.bloomers.tedxportsaidadmin.Utitltes.other;


import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bloomers.tedxportsaidadmin.model.parcel;


public class HeavilyUsed {

    public static Boolean isContextSafe(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity != null && !activity.isFinishing() && !activity.isDestroyed();
        } else {
            return activity != null && !activity.isFinishing();
        }
    }

    public static void callSaveDialog(AppCompatActivity appCompatActivity, android.support.v4.app.DialogFragment dialogFragment, parcel[] arrayList) {
        if (isContextSafe(appCompatActivity)) {
            if (arrayList != null && arrayList.length > 0) {
                Bundle bundle = new Bundle();
                for (parcel parcel : arrayList) {
                    if (parcel.getValue() instanceof Uri) {
                        bundle.putParcelable(parcel.getKey(), (Uri) parcel.getValue());
                    } else if (parcel.getValue() instanceof Integer) {
                        bundle.putInt(parcel.getKey(), (Integer) parcel.getValue());
                    } else {
                        bundle.putString(parcel.getKey(), (String) parcel.getValue());
                    }
                }
                dialogFragment.setArguments(bundle);
            }
            FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
            ft.add(dialogFragment,  null);
            ft.commitAllowingStateLoss();
        }
    }

    public static Boolean saveToRecycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            return true;
        }
        return false;
    }

    public static boolean isApi21OrMore() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}

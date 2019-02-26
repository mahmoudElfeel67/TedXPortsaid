package com.bloomers.tedxportsaidadmin;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppController extends Application {

    private static final AppController mInstance = new AppController();
    private static Boolean othersImplemented = false;
    public static final String mediumFont = "Cairo-Regular.ttf";
    public static final String smallFont = "Cairo-Light.ttf";
    public static final String bigFont = "Cairo-Bold.ttf";

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(mediumFont)
                .setFontAttrId(R.attr.fontPath)
                .build());

        Timber.plant(new Timber.DebugTree());


    }

    public Boolean isThereInternet(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return !(activeNetworkInfo == null || !activeNetworkInfo.isConnected());
    }

    public static int easyColor(Context context, int color) {
        if (context != null) {
            return ContextCompat.getColor(context, color);
        } else {
            return 0;
        }
    }

    public FragmentTransaction FragmentEase(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.anim_in,
                R.anim.anim_out,
                R.anim.anim_in,
                R.anim.anim_out);
        ft.addToBackStack(null);
        return ft;
    }

    public FragmentTransaction FragmentEaseNoAnim(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        return ft;
    }

    public FragmentTransaction FragmentEaseNoAnimNoback(FragmentActivity activity) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        return ft;
    }


    public static Locale getLocale(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("TEDX", MODE_PRIVATE);
        return new Locale(sharedPreferences.getString("language", Locale.getDefault().getLanguage()));

    }

    public void cancelAsync(AsyncTask ex) {
        if (ex != null) {
            switch (ex.getStatus()) {
                case RUNNING:
                case PENDING:
                    ex.cancel(true);
                    break;
                case FINISHED:
                    break;

            }
        }
    }

    private void removeView(@Nullable View view) {
        if (view != null && view.getParent() != null) {
            try {
                ((ViewGroup) view.getParent()).removeView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isArabic(Context mContext) {
        return mContext == null || mContext.getSharedPreferences("TEDX", MODE_PRIVATE).getString("language", Locale.getDefault().getLanguage()).equals("ar");
    }

    public void implementOthers(Activity activity) {
        if (activity == null) return;

        if (!othersImplemented) {
            othersImplemented = true;
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseDatabase.getInstance().getReference().keepSynced(true);
            SharedPreferences sharedPrefereces = activity.getSharedPreferences("TEDX", MODE_PRIVATE);
            sharedPrefereces.edit().putInt("times_opened", sharedPrefereces.getInt("times_opened", 0) + 1).apply();

        }

    }

    public void hideNavBar(Activity activity) {

        if (activity != null && hasNavBar(activity.getResources())) {
            if (Build.VERSION.SDK_INT < 19) { // lower api
                View v = activity.getWindow().getDecorView();
                v.setSystemUiVisibility(View.GONE);
            } else {
                //for new api versions.
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    private boolean hasNavBar(@NonNull Resources resources) {
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        return id > 0 && resources.getBoolean(id);
    }

    public boolean isAppInstalled(String packageName, Context context) {
        if (context == null) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void launchFacebook(Activity activity, String which, String id) {
        if (activity != null) {
            if (isAppInstalled("com.facebook.orca", activity)) {
                final String urlFb = "fb://" + which + "/" + id;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(urlFb));
                try {
                    activity.startActivity(intent);
                } catch (Exception e) {
                    //  AppController.getInstance().showErrorToast(activity);
                }

            } else {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + id)));
            }

        }
    }



    public void removeLoadingScreen(Activity activity,ViewGroup viewGroup){
        final ViewGroup rootLayout;

        if (viewGroup == null) {
            rootLayout = activity.findViewById(android.R.id.content);
        } else {
            rootLayout = viewGroup;
        }

        removeView(rootLayout.findViewById(R.id.loading_image_view));
        removeView(rootLayout.findViewById(R.id.loading_view));
    }
}

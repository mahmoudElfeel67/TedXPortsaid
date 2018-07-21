package com.bloomers.tedxportsaid;


import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
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
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppController extends MultiDexApplication {

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        Crashlytics crashlyticsKit = new Crashlytics.Builder()
             .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
             .build();

        Fabric.with(getApplicationContext(), crashlyticsKit);

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

    public  static  String parsePageHeaderInfo(Document doc) {

        Elements elements = doc.select("meta");
        String imageUrl = null;

        for (Element e : elements) {
            //fetch image url from content attribute of meta tag.
            imageUrl = e.attr("content");

            //OR more specifically you can check meta property.
            if (e.attr("property").equalsIgnoreCase("og:image")) {
                imageUrl = e.attr("content");
                break;
            }
        }
        return imageUrl;
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

    public void showErrorToast(FragmentActivity activity) {
        MainActivity.showCustomToast(activity,activity.getString(R.string.error_happend),null,false);
    }

    public void addLoadingBlock(Activity activity,ViewGroup viewGroup){
        final ViewGroup rootLayout;

        if (viewGroup == null) {
            rootLayout = activity.findViewById(android.R.id.content);
        } else {
            rootLayout = viewGroup;
        }

        View blackopack = new View(activity);
        blackopack.setBackgroundColor(easyColor(activity,R.color.white));
        blackopack.setAlpha(0);
        blackopack.setId(R.id.loading_view);
        rootLayout.addView(blackopack);
        ImageView imageView = new ImageView(activity);
        imageView.setAlpha(0F);
        imageView.setImageResource(R.drawable.xex1);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setId(R.id.loading_image_view);
        blackopack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        FrameLayout.LayoutParams pa = new FrameLayout.LayoutParams(rootLayout.getWidth(), rootLayout.getHeight());
        rootLayout.addView(imageView,pa);

        PropertyValuesHolder scalex = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.1F);
        PropertyValuesHolder scaley = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.1F);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(imageView, scalex, scaley);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(2000);
        animator.start();

        imageView.animate().setDuration(1000).alpha(.90F).start();
        blackopack.animate().setDuration(1000).alpha(.90F).start();

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

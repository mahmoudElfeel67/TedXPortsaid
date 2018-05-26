package com.bloomers.tedxportsaid;


import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bloomers.tedxportsaid.Utitltes.other.delay;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.leakcanary.LeakCanary;
import com.thefinestartist.Base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
public class AppController extends Application {

    private static final AppController mInstance = new AppController();
    private static Boolean othersImplemented = false;
    private DatabaseReference database;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static int easyColor(Context context, int color) {
        if (context != null) {
            return ContextCompat.getColor(context, color);
        } else {
            return 0;
        }
    }

    public static Boolean isntRecycled(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    public static void avoidMultiTouch(final View v) {
        v.setClickable(false);
        v.setEnabled(false);
        new delay(v, 2000) {
            @Override
            public void OnDelayEnded() {

                v.setClickable(true);
                v.setEnabled(true);
            }
        };
    }

    public static void redirectToAppWithLink(String appPackageName, @NonNull Context context, String link) {

        if (AppController.getInstance().isAppInstalled(appPackageName, context)) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, link);
            sendIntent.setType("text/plain");
            sendIntent.setPackage(appPackageName);
            context.startActivity(sendIntent);
        } else {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            context.startActivity(i);
        }
    }

    public static Locale getLocale(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("My App", MODE_PRIVATE);
        return new Locale(sharedPreferences.getString("language", Locale.getDefault().getLanguage()));

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
             .setDefaultFontPath("GE_SS_Two_Medium.otf")
             .setFontAttrId(R.attr.fontPath)
             .build());

        Timber.plant(new Timber.DebugTree());
        Base.initialize(this);
    }

    public DatabaseReference firebaseInstance() {
        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }

    public boolean isSafeRecycler(ArrayList arrayList, int postion) {
        return postion != -1 && arrayList != null && arrayList.size() > postion && arrayList.get(postion) != null;
    }

    public boolean isSafeRecycler(List arrayList, int postion) {
        try {
            return postion != -1 && arrayList != null && arrayList.size() > postion && arrayList.get(postion) != null;
        } catch (Exception e) {
            return false;
        }

    }

    public FirebaseAnalytics getAnalytics(Context context) {
        return FirebaseAnalytics.getInstance(context);
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

    public void removeView(@Nullable View view) {
        if (view != null && view.getParent() != null) {
            try {
                ((ViewGroup) view.getParent()).removeView(view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean isArabic(Context mContext) {
        return mContext == null || mContext.getSharedPreferences("My App", MODE_PRIVATE).getString("language", Locale.getDefault().getLanguage()).equals("ar");
    }

    public void deleteItem(int index, RecyclerView.Adapter adapter, ArrayList arrayList) {
        try {
            arrayList.remove(index);
            adapter.notifyItemRemoved(index);
            adapter.notifyItemRangeRemoved(index, 1);
        } catch (IndexOutOfBoundsException e) {
            adapter.notifyDataSetChanged();
            e.printStackTrace();
        }
    }

    public void implementOthers(Activity activity) {
        if (activity == null) return;

        if (!othersImplemented) {
            othersImplemented = true;
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            SharedPreferences sharedPrefereces = activity.getSharedPreferences("My App", MODE_PRIVATE);
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

    public void showNavBar(Activity activity) {
        if (hasNavBar(activity.getResources())) {

            if (Build.VERSION.SDK_INT < 19) { // lower api
                View v = activity.getWindow().getDecorView();
                v.setSystemUiVisibility(View.VISIBLE);
            } else {
                //for new api versions.
                View decorView = activity.getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    }

    public boolean hasNavBar(@NonNull Resources resources) {
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

    public String extractYTId(String ytUrl) {
        if (ytUrl != null) {

            String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed/)[^#&?]*";
            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(ytUrl);
            if (matcher.find()) {
                return matcher.group();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Boolean ifShared(Activity activity, String StringBoolean) {
        if (activity == null) {
            return false;
        }
        SharedPreferences sharedPrefereces = activity.getSharedPreferences("My App", MODE_PRIVATE);
        Boolean theBoolean = sharedPrefereces.getBoolean(StringBoolean, false);
        if (!theBoolean) {
            SharedPreferences.Editor editor = sharedPrefereces.edit();
            editor.putBoolean(StringBoolean, true);
            editor.apply();
        }
        return theBoolean;
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(activity, code, 9001);
            dlg.show();
            return false;
        } else {
            return true;
        }
    }

    public void hideSoftKeyBoard(Activity context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } else {
            context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    public void showKeyBoard(Activity activity) {
        if (activity != null && activity.getSystemService(Context.INPUT_METHOD_SERVICE) != null) {
            //noinspection ConstantConditions
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                 toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public boolean isReleaseON() {
        return !BuildConfig.DEBUG;
    }

    public void scanFile(Context context, File f) {
        Uri contentUri = Uri.fromFile(f);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public void showErrorToast(FragmentActivity activity) {
        Toast.makeText(activity, R.string.something_wrong, Toast.LENGTH_SHORT).show();
    }
}

package uz.jtscorp.namoztime;

import android.app.Application;
import android.util.Log;


import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class PrayerTimeApplication extends Application {
    private static final String TAG = "AppLifecycle";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Dastur ishga tushdi");
    }
}
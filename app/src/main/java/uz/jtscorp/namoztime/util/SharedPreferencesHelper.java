package uz.jtscorp.namoztime.util;
import android.content.Context;
import android.content.SharedPreferences;
public class SharedPreferencesHelper {
    private static final String PREF_NAME = "NamozTimes";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_MINUTE = "minute";

    public static void saveTime(Context context, int hour, int minute) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HOUR, hour);
        editor.putInt(KEY_MINUTE, minute);
        editor.apply();
    }

    public static int[] getSavedTime(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int hour = prefs.getInt(KEY_HOUR, -1);
        int minute = prefs.getInt(KEY_MINUTE, -1);
        return new int[]{hour, minute};
    }
}
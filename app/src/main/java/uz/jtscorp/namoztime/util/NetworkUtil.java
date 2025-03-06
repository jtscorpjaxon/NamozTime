package uz.jtscorp.namoztime.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

public class NetworkUtil {
    private static final String TAG = "NetworkUtil";

    // **Internet mavjudligini tekshirish**
    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                return capabilities != null &&
                        (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
            } else {
                return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
            }
        }
        return false;
    }

    // **Wi-Fi yoqish**
    public static void enableWiFi(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

    // **Mobil internet yoqish (faqat root talab qiladi)**
    public static void enableMobileData(Context context) {
        try {
            Settings.Global.putInt(context.getContentResolver(), "mobile_data", 1);
            Log.d(TAG, "Mobil internet yoqildi.");
        } catch (Exception e) {
            Log.e(TAG, "Mobil internetni yoqib bo‘lmadi!", e);
        }
    }
}
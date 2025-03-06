package uz.jtscorp.namoztime.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import uz.jtscorp.namoztime.util.NetworkUtil;

public class MobileDataReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkUtil.isInternetAvailable(context)) {
            Log.d("MobileDataReceiver", "Internet hali yo‘q, mobil internet yoqilmoqda...");
            NetworkUtil.enableMobileData(context);
        }
    }
}
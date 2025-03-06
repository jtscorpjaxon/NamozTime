package uz.jtscorp.namoztime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import uz.jtscorp.namoztime.ui.Dialogs;

public class HandInputReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new Handler(Looper.getMainLooper()).post(() -> Dialogs.showTimePicker(context));
    }
}

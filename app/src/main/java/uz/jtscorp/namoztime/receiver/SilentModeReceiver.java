package uz.jtscorp.namoztime.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

import uz.jtscorp.namoztime.util.AlarmScheduler;

public class SilentModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        Toast.makeText(context, "Telefon ovozsiz rejimga o'tdi!", Toast.LENGTH_SHORT).show();
        AlarmScheduler.scheduleNormalMode(context);
    }
}
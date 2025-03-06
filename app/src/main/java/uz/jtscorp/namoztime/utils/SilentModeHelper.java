package uz.jtscorp.namoztime.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.RequiresApi;

public class SilentModeHelper {
    private final Context context;
    private final AudioManager audioManager;
    private final NotificationManager notificationManager;
    private final PowerManager powerManager;
    private int previousRingerMode;
    private boolean isSilentModeEnabled;

    public SilentModeHelper(Context context) {
        this.context = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        this.isSilentModeEnabled = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void enableSilentMode() {
        if (!isSilentModeEnabled) {
            previousRingerMode = audioManager.getRingerMode();
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!notificationManager.isNotificationPolicyAccessGranted()) {
                    // Request notification policy access
                    // This should be handled in the UI layer
                }
            }
            
            isSilentModeEnabled = true;
        }
    }

    public void disableSilentMode() {
        if (isSilentModeEnabled) {
            audioManager.setRingerMode(previousRingerMode);
            isSilentModeEnabled = false;
        }
    }

    public boolean isSilentModeEnabled() {
        return isSilentModeEnabled;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean hasNotificationPolicyAccess() {
        return notificationManager.isNotificationPolicyAccessGranted();
    }

    public void wakeUpDevice() {
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "NamozTime::SilentModeWakeLock"
        );
        wakeLock.acquire(10000); // Wake up for 10 seconds
    }
} 
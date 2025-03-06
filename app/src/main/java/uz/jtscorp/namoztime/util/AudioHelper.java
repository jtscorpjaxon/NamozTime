package uz.jtscorp.namoztime.util;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AudioHelper {
    private final Context context;
    private final AudioManager audioManager;
    private final NotificationManager notificationManager;
    private final Handler handler;
    private int previousRingerMode;

    @Inject
    public AudioHelper(Context context) {
        this.context = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.handler = new Handler(Looper.getMainLooper());
    }

    public boolean enableSilentMode() {
        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            return false;
        }

        previousRingerMode = audioManager.getRingerMode();
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        return true;
    }

    public void disableSilentMode() {
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            audioManager.setRingerMode(previousRingerMode);
        }
    }

    public void enableSilentModeWithDuration(int durationMinutes) {
        if (enableSilentMode()) {
            handler.postDelayed(
                    this::disableSilentMode,
                    durationMinutes * 60 * 1000L
            );
        }
    }

    public boolean isInSilentMode() {
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    public boolean hasNotificationPolicyAccess() {
        return notificationManager.isNotificationPolicyAccessGranted();
    }
} 
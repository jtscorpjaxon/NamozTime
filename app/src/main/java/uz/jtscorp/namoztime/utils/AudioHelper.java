package uz.jtscorp.namoztime.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;

import javax.inject.Inject;

public class AudioHelper {
    private final Context context;
    private final AudioManager audioManager;
    private final NotificationManager notificationManager;

    @Inject
    public AudioHelper(Context context) {
        this.context = context;
        this.audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public boolean hasNotificationPolicyAccess() {
        return notificationManager.isNotificationPolicyAccessGranted();
    }

    public void enableSilentMode() {
        if (hasNotificationPolicyAccess()) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    public void disableSilentMode() {
        if (hasNotificationPolicyAccess()) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public boolean isSilentModeEnabled() {
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    public void setVolume(int streamType, int volume) {
        if (hasNotificationPolicyAccess()) {
            audioManager.setStreamVolume(
                streamType,
                volume,
                AudioManager.FLAG_SHOW_UI
            );
        }
    }

    public int getVolume(int streamType) {
        return audioManager.getStreamVolume(streamType);
    }

    public int getMaxVolume(int streamType) {
        return audioManager.getStreamMaxVolume(streamType);
    }
} 
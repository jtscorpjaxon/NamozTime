package uz.jtscorp.namoztime.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

import uz.jtscorp.namoztime.R;
import uz.jtscorp.namoztime.presentation.MainActivity;

@Singleton
public class NotificationHelper {
    private static final String CHANNEL_ID = "prayer_time_channel";
    private static final String CHANNEL_NAME = "Namoz vaqtlari";
    private static final String CHANNEL_DESCRIPTION = "Namoz vaqtlari haqida bildirishnomalar";

    private final Context context;
    private final NotificationManager notificationManager;

    @Inject
    public NotificationHelper(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showPrayerTimeNotification(String prayerName, String prayerTime) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_prayer_times)
                .setContentTitle(prayerName + " vaqti kirdi")
                .setContentText(prayerTime)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(prayerName.hashCode(), builder.build());
    }

    public void cancelNotification(String prayerName) {
        notificationManager.cancel(prayerName.hashCode());
    }

    public void cancelAllNotifications() {
        notificationManager.cancelAll();
    }
} 
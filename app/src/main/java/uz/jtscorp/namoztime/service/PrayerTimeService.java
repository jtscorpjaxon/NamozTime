package uz.jtscorp.namoztime.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import uz.jtscorp.namoztime.data.entity.PrayerTime;
import uz.jtscorp.namoztime.data.entity.Settings;
import uz.jtscorp.namoztime.data.repository.PrayerTimeRepository;
import uz.jtscorp.namoztime.data.repository.SettingsRepository;
import uz.jtscorp.namoztime.util.AudioHelper;
import uz.jtscorp.namoztime.util.NotificationHelper;

@AndroidEntryPoint
public class PrayerTimeService extends Service {
    @Inject
    PrayerTimeRepository prayerTimeRepository;

    @Inject
    SettingsRepository settingsRepository;

    @Inject
    NotificationHelper notificationHelper;

    @Inject
    AudioHelper audioHelper;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public void onCreate() {
        super.onCreate();
//        startForeground(1, notificationHelper.createServiceNotification());
        startMonitoring();
    }

    private void startMonitoring() {
        // Har minutda tekshirish
        disposables.add(Observable.interval(0, 1, TimeUnit.MINUTES)
                .subscribe(tick -> checkPrayerTimes()));
    }

    private void checkPrayerTimes() {
        Date now = new Date();
        String currentDate = dateFormat.format(now);
        String currentTime = timeFormat.format(now);

        // Juma kunini tekshirish
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        boolean isJuma = calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY;

        disposables.add(
            settingsRepository.getSettingsSync()
                .subscribe(settings -> {
                    if (isJuma && settings.isJumaSettingsEnabled()) {
                        handleJumaSettings(settings, currentTime);
                    }

                    if (settings.isNotificationsEnabled()) {
                        checkUnnotifiedPrayerTimes(settings, currentDate, currentTime);
                    }
                })
        );
    }

    private void handleJumaSettings(Settings settings, String currentTime) {
//        if (isTimeBetween(currentTime, settings.getJumaStartTime(), settings.getJumaEndTime())) {
//            if (!audioHelper.isSilentModeEnabled()) {
//                audioHelper.enableSilentMode();
//            }
//        } else {
//            if (audioHelper.isSilentModeEnabled()) {
//                audioHelper.disableSilentMode();
//            }
//        }
    }

    private void checkUnnotifiedPrayerTimes(Settings settings, String currentDate, String currentTime) {
        disposables.add(
            prayerTimeRepository.getUnnotifiedPrayerTimes(currentDate, currentTime)
                .subscribe(prayerTimes -> {
                    for (PrayerTime prayerTime : prayerTimes) {
                        handlePrayerTime(prayerTime, settings);
                    }
                })
        );
    }

    private void handlePrayerTime(PrayerTime prayerTime, Settings settings) {
        // Bildirishnoma yuborish
        notificationHelper.showPrayerTimeNotification(prayerTime.getName(), prayerTime.getTime());

        // Ovozsiz rejimni yoqish
        if (settings.isSilentModeEnabled() && audioHelper.hasNotificationPolicyAccess()) {
            audioHelper.enableSilentMode();
            // Belgilangan vaqtdan keyin ovozsiz rejimni o'chirish
            disposables.add(
                Observable.timer(settings.getDefaultSilentModeDuration(), TimeUnit.MINUTES)
                    .subscribe(tick -> audioHelper.disableSilentMode())
            );
        }

        // Bildirishnoma yuborilganini belgilash
//        disposables.add(
//            prayerTimeRepository.markPrayerTimeAsNotified(prayerTime.getId())
//                .subscribe()
//        );
    }

    private boolean isTimeBetween(String currentTime, String startTime, String endTime) {
        return currentTime.compareTo(startTime) >= 0 && currentTime.compareTo(endTime) <= 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public String getCurrentPrayerTime() {
        return "";
    }

    public void updatePrayerTimes(double latitude, double longitude) {
    }
}
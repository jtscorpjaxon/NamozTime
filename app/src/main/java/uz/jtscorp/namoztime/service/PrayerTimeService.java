package uz.jtscorp.namoztime.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.lifecycle.LifecycleService;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.data.model.PrayerTime;
import uz.jtscorp.namoztime.domain.repository.NotificationSettingsRepository;
import uz.jtscorp.namoztime.domain.repository.PrayerTimeRepository;
import uz.jtscorp.namoztime.utils.NotificationHelper;
import uz.jtscorp.namoztime.utils.SilentModeHelper;

@AndroidEntryPoint
public class PrayerTimeService extends LifecycleService {
    @Inject
    PrayerTimeRepository prayerTimeRepository;

    @Inject
    NotificationSettingsRepository notificationSettingsRepository;

    @Inject
    NotificationHelper notificationHelper;

    @Inject
    SilentModeHelper silentModeHelper;

    private Timer timer;
    private TimerTask currentTask;

    @Override
    public void onCreate() {
        super.onCreate();
        startPrayerTimeMonitoring();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    private void startPrayerTimeMonitoring() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();

        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();

        prayerTimeRepository.getPrayerTimesByDateRange(startDate, endDate)
                .observeForever(prayerTimes -> {
                    if (prayerTimes != null) {
                        for (PrayerTime prayerTime : prayerTimes) {
                            schedulePrayerTimeNotifications(prayerTime);
                        }
                    }
                });

    }

    private void schedulePrayerTimeNotifications(PrayerTime prayerTime) {
        notificationSettingsRepository.getSettings().observe(this, settings -> {
            if (settings != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(prayerTime.getDate());

                // Schedule Fajr notifications
                scheduleNotification(prayerTime.getFajr(), "Bomdod", settings.getFajrReminderMinutes());

                // Schedule Dhuhr notifications
                scheduleNotification(prayerTime.getDhuhr(), "Peshin", settings.getDhuhrReminderMinutes());

                // Schedule Asr notifications
                scheduleNotification(prayerTime.getAsr(), "Asr", settings.getAsrReminderMinutes());

                // Schedule Maghrib notifications
                scheduleNotification(prayerTime.getMaghrib(), "Shom", settings.getMaghribReminderMinutes());

                // Schedule Isha notifications
                scheduleNotification(prayerTime.getIsha(), "Xufton", settings.getIshaReminderMinutes());

                // Schedule Juma silent mode if enabled
                if (settings.isJumaEnabled() && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                    scheduleJumaSilentMode(settings.getJumaStartTime(), settings.getJumaEndTime());
                }
            }
        });
    }

    private void scheduleNotification(Date prayerTime, String prayerName, int reminderMinutes) {
        Date currentTime = new Date();
        long timeDiff = prayerTime.getTime() - currentTime.getTime();
        long reminderTime = timeDiff - (reminderMinutes * 60 * 1000L);

        if (reminderTime > 0) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    notificationHelper.showPrayerTimeNotification(prayerName, String.valueOf(reminderMinutes));
                    silentModeHelper.enableSilentMode();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            silentModeHelper.disableSilentMode();
                        }
                    }, reminderMinutes * 60 * 1000L);
                }
            };
            timer.schedule(task, reminderTime);
        }
    }

    private void scheduleJumaSilentMode(String startTime, String endTime) {
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");
        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        calendar.set(Calendar.HOUR_OF_DAY, startHour);
        calendar.set(Calendar.MINUTE, startMinute);
        calendar.set(Calendar.SECOND, 0);

        long startTimeInMillis = calendar.getTimeInMillis();
        long timeDiff = startTimeInMillis - currentTime.getTime();

        if (timeDiff > 0) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    silentModeHelper.enableSilentMode();

                    calendar.set(Calendar.HOUR_OF_DAY, endHour);
                    calendar.set(Calendar.MINUTE, endMinute);
                    calendar.set(Calendar.SECOND, 0);

                    long endTimeInMillis = calendar.getTimeInMillis();
                    long duration = endTimeInMillis - startTimeInMillis;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            silentModeHelper.disableSilentMode();
                        }
                    }, duration);
                }
            };
            timer.schedule(task, timeDiff);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public String getCurrentPrayerTime() {
        AtomicReference<String> result = new AtomicReference<>("");
        prayerTimeRepository.getCurrentPrayerTime().observe(this, prayerTime -> {
            if (prayerTime != null) {
                //current prayer time
                result.set(this.getPrayTime(prayerTime, new Time(System.currentTimeMillis())));
            }
        });
        return result.get();
    }

    public void updatePrayerTimes(double latitude, double longitude) {
    }

    public String getNextPrayTime(String currentPrayTime){
        return   switch(currentPrayTime){
            case "Bomdod" -> "Quyosh";
            case "Quyosh" -> "Peshin";
            case "Peshin" -> "Asr";
            case "Asr" -> "Shom";
            case "Shom" -> "Xufton";
            default -> "Bomdod";
        };
    }
    public String getPrayTime(PrayerTime prayerTime, Time currentTime){
        String currentPrayTime;
        //check current time with prayer time
        if(currentTime.after(prayerTime.getFajr()) && currentTime.before(prayerTime.getSunrise())){
            currentPrayTime = "Bomdod";
        }else if(currentTime.after(prayerTime.getSunrise()) && currentTime.before(prayerTime.getDhuhr())){
            currentPrayTime = "Quyosh";
        }else if (currentTime.after(prayerTime.getDhuhr()) && currentTime.before(prayerTime.getAsr())){
            currentPrayTime = "Peshin";
        }else if (currentTime.after(prayerTime.getAsr()) && currentTime.before(prayerTime.getMaghrib())){
            currentPrayTime = "Asr";
        }else if (currentTime.after(prayerTime.getMaghrib()) && currentTime.before(prayerTime.getIsha())){
            currentPrayTime = "Shom";
        }else {
            currentPrayTime = "Xufton";
        }
        return currentPrayTime;
    }
}
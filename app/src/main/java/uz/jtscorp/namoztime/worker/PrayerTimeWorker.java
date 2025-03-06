package uz.jtscorp.namoztime.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import uz.jtscorp.namoztime.data.repository.PrayerTimeRepository;
import uz.jtscorp.namoztime.data.repository.SettingsRepository;

@HiltWorker
public class PrayerTimeWorker extends Worker {
    private final PrayerTimeRepository prayerTimeRepository;
    private final SettingsRepository settingsRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @AssistedInject
    public PrayerTimeWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters params,
            PrayerTimeRepository prayerTimeRepository,
            SettingsRepository settingsRepository) {
        super(context, params);
        this.prayerTimeRepository = prayerTimeRepository;
        this.settingsRepository = settingsRepository;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            disposables.add(
                settingsRepository.getSettingsSync()
                    .flatMapCompletable(settings ->
                        prayerTimeRepository.updatePrayerTimes(
                            settings.getLatitude(),
                            settings.getLongitude()
                        )
                    )
                    .blockingAwait()
            );
            return Result.success();
        } catch (Exception e) {
            return Result.retry();
        } finally {
            disposables.clear();
        }
    }

    public static void schedule(Context context) {
        // Har kuni soat 00:00 da ishga tushadi
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // Birinchi marta hozir ishga tushirish
        OneTimeWorkRequest initialWork = new OneTimeWorkRequest.Builder(PrayerTimeWorker.class)
                .setConstraints(constraints)
                .build();

        // Har kunlik yangilash
        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(
                PrayerTimeWorker.class,
                24, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(
                        calendar.getTimeInMillis() - System.currentTimeMillis(),
                        TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .beginWith(initialWork)
                .then(periodicWork)
                .enqueue();
    }
}
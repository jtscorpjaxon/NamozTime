package uz.jtscorp.namoztime.worker;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.hilt.work.HiltWorker;
import androidx.work.Constraints;
import androidx.work.CoroutineWorker;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.concurrent.TimeUnit;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import kotlin.coroutines.Continuation;
import uz.jtscorp.namoztime.domain.repository.PrayerTimeRepository;

@HiltWorker
public class PrayerTimeWorker extends CoroutineWorker {
    private final PrayerTimeRepository repository;
    private final FusedLocationProviderClient fusedLocationClient;

    @AssistedInject
    public PrayerTimeWorker(
        @Assisted Context appContext,
        @Assisted WorkerParameters workerParams,
        PrayerTimeRepository repository,
        FusedLocationProviderClient fusedLocationClient
    ) {
        super(appContext, workerParams);
        this.repository = repository;
        this.fusedLocationClient = fusedLocationClient;
    }


    private void scheduleNextUpdate() {
        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

        OneTimeWorkRequest nextWork = new OneTimeWorkRequest.Builder(PrayerTimeWorker.class)
            .setConstraints(constraints)
            .setInitialDelay(24, TimeUnit.HOURS)
            .build();

        WorkManager.getInstance(getApplicationContext())
            .enqueueUniqueWork(
                "prayer_time_update",
                ExistingWorkPolicy.REPLACE,
                nextWork
            );
    }

    public static void schedule(Context context) {
        Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(PrayerTimeWorker.class)
            .setConstraints(constraints)
            .build();

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "prayer_time_update",
                ExistingWorkPolicy.REPLACE,
                workRequest
            );
    }

    @Nullable
    @Override
    public Object doWork(@NonNull Continuation<? super Result> continuation) {
        try {
            // Get location
            if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return Result.failure();
            }
            Task<Location> locationTask = fusedLocationClient.getLastLocation();
            Location location = Tasks.await(locationTask);

            // Update prayer times
            repository.updatePrayerTimes(
                    location != null ? location.getLatitude() : 40.8487, // Default: Olmaliq coordinates
                    location != null ? location.getLongitude() : 69.5983
            );

            // Schedule next update
            scheduleNextUpdate();

            return Result.success();
        } catch (Exception e) {
            return Result.retry();
        }
    }
}
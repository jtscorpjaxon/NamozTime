package uz.jtscorp.namoztime.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import uz.jtscorp.namoztime.data.local.AppDatabase;
import uz.jtscorp.namoztime.data.local.NotificationSettingsDao;
import uz.jtscorp.namoztime.data.local.PrayerTimeDao;
import uz.jtscorp.namoztime.data.repository.NotificationSettingsRepositoryImpl;
import uz.jtscorp.namoztime.data.repository.PrayerTimeRepositoryImpl;
import uz.jtscorp.namoztime.domain.repository.NotificationSettingsRepository;
import uz.jtscorp.namoztime.domain.repository.PrayerTimeRepository;
import uz.jtscorp.namoztime.utils.AudioHelper;
import uz.jtscorp.namoztime.utils.NotificationHelper;
import uz.jtscorp.namoztime.utils.SilentModeHelper;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(
        @ApplicationContext Context context
    ) {
        return Room.databaseBuilder(
            context,
            AppDatabase.class,
            "prayer_time_db"
        ).build();
    }

    @Provides
    @Singleton
    public PrayerTimeDao providePrayerTimeDao(AppDatabase db) {
        return db.prayerTimeDao();
    }

    @Provides
    @Singleton
    public NotificationSettingsDao provideNotificationSettingsDao(AppDatabase db) {
        return db.notificationSettingsDao();
    }

    @Provides
    @Singleton
    public PrayerTimeRepository providePrayerTimeRepository(
        PrayerTimeDao dao,
        uz.jtscorp.namoztime.data.api.PrayerTimesApi api
    ) {
        return new PrayerTimeRepositoryImpl(api, dao);
    }

    @Provides
    @Singleton
    public NotificationSettingsRepository provideNotificationSettingsRepository(
        NotificationSettingsDao dao
    ) {
        return new NotificationSettingsRepositoryImpl(dao);
    }

    @Provides
    @Singleton
    public FusedLocationProviderClient provideFusedLocationClient(
        @ApplicationContext Context context
    ) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    @Provides
    @Singleton
    public NotificationHelper provideNotificationHelper(
        @ApplicationContext Context context
    ) {
        return new NotificationHelper(context);
    }

    @Provides
    @Singleton
    public SilentModeHelper provideSilentModeHelper(
        @ApplicationContext Context context
    ) {
        return new SilentModeHelper(context);
    }

    @Provides
    @Singleton
    public AudioHelper provideAudioHelper(
        @ApplicationContext Context context
    ) {
        return new AudioHelper(context);
    }
} 
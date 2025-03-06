package uz.jtscorp.namoztime.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import uz.jtscorp.namoztime.data.dao.PrayerTimeDao;
import uz.jtscorp.namoztime.data.dao.SettingsDao;
import uz.jtscorp.namoztime.data.db.AppDatabase;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase provideDatabase(@ApplicationContext Context context) {
        return AppDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public PrayerTimeDao providePrayerTimeDao(AppDatabase database) {
        return database.prayerTimeDao();
    }

    @Provides
    @Singleton
    public SettingsDao provideSettingsDao(AppDatabase database) {
        return database.settingsDao();
    }
} 
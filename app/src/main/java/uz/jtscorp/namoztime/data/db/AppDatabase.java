package uz.jtscorp.namoztime.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import uz.jtscorp.namoztime.data.dao.PrayerTimeDao;
import uz.jtscorp.namoztime.data.dao.SettingsDao;
import uz.jtscorp.namoztime.data.entity.PrayerTime;
import uz.jtscorp.namoztime.data.entity.Settings;

@Database(entities = {PrayerTime.class, Settings.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract PrayerTimeDao prayerTimeDao();
    public abstract SettingsDao settingsDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "namoztime.db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();

                    // Dastlabki sozlamalarni o'rnatish
                    INSTANCE.getTransactionExecutor().execute(() -> {
                        SettingsDao settingsDao = INSTANCE.settingsDao();
                        if (settingsDao.getSettingsSync() == null) {
                            settingsDao.insert(new Settings());
                        }
                    });
                }
            }
        }
        return INSTANCE;
    }
} 
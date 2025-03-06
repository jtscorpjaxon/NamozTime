package uz.jtscorp.namoztime.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import uz.jtscorp.namoztime.data.model.NotificationSettings;
import uz.jtscorp.namoztime.data.model.PrayerTime;
import uz.jtscorp.namoztime.utils.Converters;

@Database(
    entities = {
        PrayerTime.class,
        NotificationSettings.class
    },
    version = 1,exportSchema = false
)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PrayerTimeDao prayerTimeDao();
    public abstract NotificationSettingsDao notificationSettingsDao();
}


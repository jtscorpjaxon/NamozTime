package uz.jtscorp.namoztime.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import uz.jtscorp.namoztime.data.entity.Settings;

@Dao
public interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Settings settings);

    @Update
    void update(Settings settings);

    @Query("SELECT * FROM settings WHERE id = 1")
    LiveData<Settings> getSettings();

    @Query("SELECT * FROM settings WHERE id = 1")
    Settings getSettingsSync();

    @Query("UPDATE settings SET notificationsEnabled = :enabled WHERE id = 1")
    void updateNotificationsEnabled(boolean enabled);

    @Query("UPDATE settings SET silentModeEnabled = :enabled WHERE id = 1")
    void updateSilentModeEnabled(boolean enabled);

    @Query("UPDATE settings SET jumaSettingsEnabled = :enabled WHERE id = 1")
    void updateJumaSettingsEnabled(boolean enabled);

    @Query("UPDATE settings SET location = :location, latitude = :latitude, longitude = :longitude WHERE id = 1")
    void updateLocation(String location, double latitude, double longitude);

    @Query("UPDATE settings SET defaultNotificationDelay = :delay WHERE id = 1")
    void updateDefaultNotificationDelay(int delay);

    @Query("UPDATE settings SET defaultSilentModeDuration = :duration WHERE id = 1")
    void updateDefaultSilentModeDuration(int duration);

    @Query("UPDATE settings SET jumaStartTime = :startTime, jumaEndTime = :endTime WHERE id = 1")
    void updateJumaTimes(String startTime, String endTime);
} 
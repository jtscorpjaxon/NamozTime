package uz.jtscorp.namoztime.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import uz.jtscorp.namoztime.data.model.NotificationSettings;

@Dao
public interface NotificationSettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NotificationSettings settings);

    @Update
    void update(NotificationSettings settings);

    @Delete
    void delete(NotificationSettings settings);

    @Query("SELECT * FROM notification_settings LIMIT 1")
    LiveData<NotificationSettings> getSettings();

}
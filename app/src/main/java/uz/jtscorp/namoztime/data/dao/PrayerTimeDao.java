package uz.jtscorp.namoztime.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uz.jtscorp.namoztime.data.entity.PrayerTime;

@Dao
public interface PrayerTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PrayerTime prayerTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PrayerTime> prayerTimes);

    @Update
    void update(PrayerTime prayerTime);

    @Query("SELECT * FROM prayer_times WHERE date = :date ORDER BY time ASC")
    LiveData<List<PrayerTime>> getPrayerTimesForDate(String date);

    @Query("SELECT * FROM prayer_times WHERE date = :date AND time > :currentTime ORDER BY time ASC LIMIT 1")
    LiveData<PrayerTime> getNextPrayerTime(String date, String currentTime);

    @Query("SELECT * FROM prayer_times WHERE date = :date AND time <= :currentTime ORDER BY time DESC LIMIT 1")
    LiveData<PrayerTime> getCurrentPrayerTime(String date, String currentTime);

    @Query("DELETE FROM prayer_times WHERE date <= :date")
    void deleteOldPrayerTimes(String date);

    @Query("SELECT * FROM prayer_times WHERE date = :date AND isNotified = 0 AND time > :currentTime ORDER BY time ASC")
    List<PrayerTime> getUnnotifiedPrayerTimes(String date, String currentTime);

    @Query("SELECT * FROM prayer_times")
    LiveData<List<PrayerTime>> getAllPrayerTimes();
} 
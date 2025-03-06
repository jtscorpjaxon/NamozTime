package uz.jtscorp.namoztime.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import uz.jtscorp.namoztime.data.model.PrayerTime;

@Dao
public interface PrayerTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PrayerTime prayerTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PrayerTime> prayerTimes);

    @Update
    void update(PrayerTime prayerTime);

    @Delete
    void delete(PrayerTime prayerTime);

    @Query("DELETE FROM prayer_times")
    void deleteAll();

    @Query("SELECT * FROM prayer_times")
    LiveData<List<PrayerTime>> getAllPrayerTimes();

    @Query("SELECT * FROM prayer_times WHERE date = :date")
    LiveData<PrayerTime> getPrayerTimeByDate(Date date);

    @Query("SELECT * FROM prayer_times WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    LiveData<List<PrayerTime>> getPrayerTimesBetweenDates(Date startDate, Date endDate);

    @Query("SELECT * FROM prayer_times WHERE date = :currentDate ORDER BY date ASC LIMIT 1")
    LiveData<PrayerTime> getCurrentPrayerTime(Date currentDate);


}
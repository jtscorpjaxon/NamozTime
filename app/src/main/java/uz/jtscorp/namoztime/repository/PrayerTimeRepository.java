package uz.jtscorp.namoztime.repository;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import uz.jtscorp.namoztime.data.model.PrayerTime;

public interface PrayerTimeRepository {
    void updatePrayerTimes(List<PrayerTime> prayerTimes);
    PrayerTime getPrayerTimeByDate(Date date);
    List<PrayerTime> getPrayerTimes();
    void deleteAllPrayerTimes();

    List<PrayerTime> getPrayerTimesByDateRange(Date startDate, Date endDate);

    void updatePrayerTimes(double latitude, double longitude);

    void deleteOldPrayerTimes(Date date);

    LiveData<PrayerTime> getCurrentPrayerTime();

    LiveData<PrayerTime> getNextPrayerTime();

    LiveData<List<PrayerTime>> getAllPrayerTimes();
}
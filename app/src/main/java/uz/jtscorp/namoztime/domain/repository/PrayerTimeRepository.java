package uz.jtscorp.namoztime.domain.repository;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import uz.jtscorp.namoztime.data.model.PrayerTime;

public interface PrayerTimeRepository {
    void updatePrayerTimes(List<PrayerTime> prayerTimes);
    PrayerTime getPrayerTimeByDate(Date date);
    LiveData<List<PrayerTime>> getPrayerTimes();
    void deleteAllPrayerTimes();

    LiveData<List<PrayerTime>> getPrayerTimesByDateRange(Date startDate, Date endDate);

    void updatePrayerTimes(double latitude, double longitude);

    void deleteOldPrayerTimes(Date date);

    LiveData<PrayerTime> getCurrentPrayerTime();


}
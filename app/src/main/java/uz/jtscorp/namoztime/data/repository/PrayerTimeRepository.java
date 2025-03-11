package uz.jtscorp.namoztime.data.repository;

import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.jtscorp.namoztime.data.api.PrayerTimesApi;
import uz.jtscorp.namoztime.data.dao.PrayerTimeDao;
import uz.jtscorp.namoztime.data.entity.PrayerTime;
import uz.jtscorp.namoztime.data.mapper.PrayerTimeMapper;
import uz.jtscorp.namoztime.data.model.api.PrayerTimesResponse;

@Singleton
public class PrayerTimeRepository {
    private final PrayerTimesApi api;
    private final PrayerTimeDao dao;
    private final PrayerTimeMapper mapper;

    @Inject
    public PrayerTimeRepository(PrayerTimesApi api, PrayerTimeDao dao, PrayerTimeMapper mapper) {
        this.api = api;
        this.dao = dao;
        this.mapper = mapper;
    }

    public Completable updatePrayerTimes(double latitude, double longitude) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        //get response from api
        return api.getPrayerTimes(latitude, longitude, 3, month, year)
                .map(mapper::mapToPrayerTimes)
                .flatMapCompletable(prayerTimes -> Completable.fromAction(() -> {
                    dao.deleteOldPrayerTimes(prayerTimes.get(0).getDate());
                    dao.insertAll(prayerTimes);
                }))
                .subscribeOn(Schedulers.io());
    }

    public LiveData<List<PrayerTime>> getPrayerTimesForDate(String date) {
        return dao.getPrayerTimesForDate(date);
    }

    public LiveData<PrayerTime> getCurrentPrayerTime(String date, String currentTime) {
        return dao.getCurrentPrayerTime(date, currentTime);
    }

    public LiveData<PrayerTime> getNextPrayerTime(String date, String currentTime) {
        return dao.getNextPrayerTime(date, currentTime);
    }

    public Single<List<PrayerTime>> getUnnotifiedPrayerTimes(String date, String currentTime) {
        return Single.fromCallable(() -> dao.getUnnotifiedPrayerTimes(date, currentTime))
                .subscribeOn(Schedulers.io());
    }

    public Completable updatePrayerTime(PrayerTime prayerTime) {
        return Completable.fromAction(() -> dao.update(prayerTime))
                .subscribeOn(Schedulers.io());
    }
} 

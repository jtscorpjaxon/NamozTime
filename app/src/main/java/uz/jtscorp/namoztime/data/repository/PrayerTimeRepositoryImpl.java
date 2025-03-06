package uz.jtscorp.namoztime.data.repository;

import androidx.lifecycle.LiveData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.jtscorp.namoztime.data.local.PrayerTimeDao;
import uz.jtscorp.namoztime.data.model.PrayerTime;
import uz.jtscorp.namoztime.data.remote.PrayerTimeApi;
import uz.jtscorp.namoztime.data.remote.PrayerTimeData;
import uz.jtscorp.namoztime.data.remote.PrayerTimeResponse;
import uz.jtscorp.namoztime.domain.repository.PrayerTimeRepository;

public class PrayerTimeRepositoryImpl implements PrayerTimeRepository {
    private final PrayerTimeApi api;
    private final PrayerTimeDao dao;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat timeFormat;

    @Inject
    public PrayerTimeRepositoryImpl(PrayerTimeApi api, PrayerTimeDao dao) {
        this.api = api;
        this.dao = dao;
        this.dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @Override
    public LiveData<List<PrayerTime>> getPrayerTimesByDateRange(Date startDate, Date endDate) {
        return  dao.getPrayerTimesBetweenDates(startDate, endDate);
    }

    @Override
    public void updatePrayerTimes(List<PrayerTime> prayerTimes) {
        dao.insertAll(prayerTimes);
    }

    @Override
    public PrayerTime getPrayerTimeByDate(Date date) {
        return dao.getPrayerTimeByDate(date).getValue();
    }

    @Override
    public LiveData<List<PrayerTime>> getPrayerTimes() {
        return dao.getAllPrayerTimes();
    }

    @Override
    public void deleteAllPrayerTimes() {
        dao.deleteAll();
    }

    @Override
    public void updatePrayerTimes(double latitude, double longitude) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        api.getPrayerTimes(latitude, longitude, month, year, 13, 1, "Asia/Tashkent")
            .enqueue(new Callback<PrayerTimeResponse>() {

                @Override
                public void onResponse(Call<PrayerTimeResponse> call, Response<PrayerTimeResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<PrayerTime> prayerTimes = new ArrayList<>();
                        for (PrayerTimeData data : response.body().getData()) {
                            try {
                                PrayerTime prayerTime = new PrayerTime();
                                prayerTime.setDate(data.getDate());
                                prayerTime.setFajr(data.getTimings().getFajr());
                                prayerTime.setSunrise(data.getTimings().getSunrise());
                                prayerTime.setDhuhr(data.getTimings().getDhuhr());
                                prayerTime.setAsr(data.getTimings().getAsr());
                                prayerTime.setMaghrib(data.getTimings().getMaghrib());
                                prayerTime.setIsha(data.getTimings().getIsha());
                                prayerTimes.add(prayerTime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<PrayerTimeResponse> call, Throwable t) {
                    t.printStackTrace();

                }
            });
    }

    @Override
    public void deleteOldPrayerTimes(Date date) {
        PrayerTime prayerTime= dao.getPrayerTimeByDate(date).getValue();
        dao.delete(prayerTime);
    }

    @Override
    public LiveData<PrayerTime> getCurrentPrayerTime() {
        return dao.getCurrentPrayerTime(new Date());
    }



} 
package uz.jtscorp.namoztime.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.jtscorp.namoztime.data.local.PrayerTimeDao;
import uz.jtscorp.namoztime.data.model.PrayerTime;
import uz.jtscorp.namoztime.data.remote.PrayerTimeApi;
import uz.jtscorp.namoztime.data.remote.PrayerTimeResponse;
import uz.jtscorp.namoztime.data.remote.PrayerTimeData;

@Singleton
public class PrayerTimeRepository {
    private final PrayerTimeApi api;
    private final PrayerTimeDao dao;
    private final MutableLiveData<PrayerTime> currentPrayerTime = new MutableLiveData<>();
    private final MutableLiveData<PrayerTime> nextPrayerTime = new MutableLiveData<>();

    @Inject
    public PrayerTimeRepository(PrayerTimeApi api, PrayerTimeDao dao) {
        this.api = api;
        this.dao = dao;
    }



    public void updatePrayerTimes(double latitude, double longitude) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        
        api.getPrayerTimes(
            latitude,
            longitude,
            month,
            year,
            5, // Hanafi method
            1, // Hanafi school
            "Asia/Tashkent"
        ).enqueue(new Callback<PrayerTimeResponse>() {
            @Override
            public void onResponse(Call<PrayerTimeResponse> call, Response<PrayerTimeResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    for (PrayerTimeData data : response.body().getData()) {
                        PrayerTime prayerTime = new PrayerTime();
                        prayerTime.setDate(data.getDate());
                        //convert String to Date
                        prayerTime.setFajr(data.getTimings().getFajr());
                        prayerTime.setSunrise(data.getTimings().getSunrise());
                        prayerTime.setDhuhr(data.getTimings().getDhuhr());
                        prayerTime.setAsr(data.getTimings().getAsr());
                        prayerTime.setMaghrib(data.getTimings().getMaghrib());
                        prayerTime.setIsha(data.getTimings().getIsha());
                        dao.insert(prayerTime);
                    }
                    updateCurrentAndNextPrayerTimes();
                }
            }

            @Override
            public void onFailure(Call<PrayerTimeResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateCurrentAndNextPrayerTimes() {
        Date now = new Date();
        PrayerTime todayPrayer = dao.getPrayerTimeByDate(now).getValue();
        if (todayPrayer != null) {
            Calendar calendar = Calendar.getInstance();
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            String[] times = {
                    String.valueOf(todayPrayer.getFajr()),
                    String.valueOf(todayPrayer.getSunrise()),
                    String.valueOf(todayPrayer.getDhuhr()),
                    String.valueOf(todayPrayer.getAsr()),
                    String.valueOf(todayPrayer.getMaghrib()),
                    String.valueOf(todayPrayer.getIsha())
            };

            String[] names = {"Bomdod", "Quyosh", "Peshin", "Asr", "Shom", "Xufton"};

            PrayerTime current = null;
            PrayerTime next = null;

            for (int i = 0; i < times.length; i++) {
                String[] timeParts = times[i].split(":");
                int hour = Integer.parseInt(timeParts[0]);
                int minute = Integer.parseInt(timeParts[1]);

                if (currentHour < hour || (currentHour == hour && currentMinute < minute)) {
                    if (i > 0) {
                        current = new PrayerTime();
                        current.setName(names[i - 1]);
                        current.setTime(times[i - 1]);
                    }
                    next = new PrayerTime();
                    next.setName(names[i]);
                    next.setTime(times[i]);
                    break;
                }
            }

            if (current == null && next == null) {
                current = new PrayerTime();
                current.setName(names[names.length - 1]);
                current.setTime(times[times.length - 1]);

                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.DAY_OF_MONTH, 1);
                PrayerTime tomorrowPrayer = dao.getPrayerTimeByDate(tomorrow.getTime()).getValue();
                if (tomorrowPrayer != null) {
                    next = new PrayerTime();
                    next.setName(names[0]);
                    next.setTime(tomorrowPrayer.getFajr().toString());
                }
            }

            currentPrayerTime.postValue(current);
            nextPrayerTime.postValue(next);
        }
    }

    public LiveData<PrayerTime> getCurrentPrayerTime() {
        return currentPrayerTime;
    }

    public LiveData<PrayerTime> getNextPrayerTime() {
        return nextPrayerTime;
    }

    public LiveData<PrayerTime> getPrayerTimeByDate(Date date) {
        return dao.getPrayerTimeByDate(date);
    }

    public LiveData<List<PrayerTime>> getAllPrayerTimes() {
        return dao.getAllPrayerTimes();
    }

    public void insertPrayerTime(PrayerTime prayerTime) {
        dao.insert(prayerTime);
        updateCurrentAndNextPrayerTimes();
    }

    public void updatePrayerTime(PrayerTime prayerTime) {
        dao.update(prayerTime);
        updateCurrentAndNextPrayerTimes();
    }

    public void deletePrayerTime(PrayerTime prayerTime) {
        dao.delete(prayerTime);
        updateCurrentAndNextPrayerTimes();
    }

    public void deleteAllPrayerTimes() {
        dao.deleteAll();
        currentPrayerTime.postValue(null);
        nextPrayerTime.postValue(null);
    }
} 

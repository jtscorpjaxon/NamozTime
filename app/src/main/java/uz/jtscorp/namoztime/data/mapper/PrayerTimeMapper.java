package uz.jtscorp.namoztime.data.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import uz.jtscorp.namoztime.data.entity.PrayerTime;
import uz.jtscorp.namoztime.data.model.api.PrayerTimesResponse;

@Singleton
public class PrayerTimeMapper {

    @Inject
    public PrayerTimeMapper() {}

    public List<PrayerTime> mapToPrayerTimes(PrayerTimesResponse response) {
        List<PrayerTime> prayerTimes = new ArrayList<>();
        PrayerTimesResponse.Timings timings = response.getData().getTimings();
        String date = response.getData().getDate().getGregorian().getDate();

        // Bomdod
        prayerTimes.add(new PrayerTime("Bomdod", timings.getFajr(), date));

        // Quyosh
        prayerTimes.add(new PrayerTime("Quyosh", timings.getSunrise(), date));

        // Peshin
        prayerTimes.add(new PrayerTime("Peshin", timings.getDhuhr(), date));

        // Asr
        prayerTimes.add(new PrayerTime("Asr", timings.getAsr(), date));

        // Shom
        prayerTimes.add(new PrayerTime("Shom", timings.getMaghrib(), date));

        // Xufton
        prayerTimes.add(new PrayerTime("Xufton", timings.getIsha(), date));

        return prayerTimes;
    }

    public List<PrayerTime> mapToMonthlyPrayerTimes(List<PrayerTimesResponse> responses) {
        List<PrayerTime> allPrayerTimes = new ArrayList<>();
        for (PrayerTimesResponse response : responses) {
            allPrayerTimes.addAll(mapToPrayerTimes(response));
        }
        return allPrayerTimes;
    }
} 
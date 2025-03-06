package uz.jtscorp.namoztime.data.api;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import uz.jtscorp.namoztime.data.model.api.PrayerTimesResponse;

public interface PrayerTimesApi {
    @GET("v1/timings")
    Single<PrayerTimesResponse> getPrayerTimes(
        @Query("latitude") double latitude,
        @Query("longitude") double longitude,
        @Query("method") int method, // 3 = Muslim World League
        @Query("month") int month,
        @Query("year") int year
    );

    @GET("v1/calendar")
    Single<PrayerTimesResponse> getMonthlyPrayerTimes(
        @Query("latitude") double latitude,
        @Query("longitude") double longitude,
        @Query("method") int method,
        @Query("month") int month,
        @Query("year") int year
    );
} 
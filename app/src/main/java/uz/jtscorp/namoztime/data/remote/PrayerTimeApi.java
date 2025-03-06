package uz.jtscorp.namoztime.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PrayerTimeApi {
    @GET("v1/calendar")
    Call<PrayerTimeResponse> getPrayerTimes(
        @Query("latitude") double latitude,
        @Query("longitude") double longitude,
        @Query("month") int month,
        @Query("year") int year,
        @Query("method") int method,
        @Query("school") int school,
        @Query("timezonestring") String timezone
    );
} 
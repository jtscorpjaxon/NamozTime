package uz.jtscorp.namoztime.data.model.api;

import com.google.gson.annotations.SerializedName;

public class PrayerTimesResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private PrayerData data;

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public PrayerData getData() {
        return data;
    }

    public static class PrayerData {
        @SerializedName("timings")
        private Timings timings;

        @SerializedName("date")
        private DateInfo date;

        public Timings getTimings() {
            return timings;
        }

        public DateInfo getDate() {
            return date;
        }
    }

    public static class Timings {
        @SerializedName("Fajr")
        private String fajr;

        @SerializedName("Sunrise")
        private String sunrise;

        @SerializedName("Dhuhr")
        private String dhuhr;

        @SerializedName("Asr")
        private String asr;

        @SerializedName("Maghrib")
        private String maghrib;

        @SerializedName("Isha")
        private String isha;

        public String getFajr() {
            return fajr;
        }

        public String getSunrise() {
            return sunrise;
        }

        public String getDhuhr() {
            return dhuhr;
        }

        public String getAsr() {
            return asr;
        }

        public String getMaghrib() {
            return maghrib;
        }

        public String getIsha() {
            return isha;
        }
    }

    public static class DateInfo {
        @SerializedName("readable")
        private String readable;

        @SerializedName("gregorian")
        private Gregorian gregorian;

        public String getReadable() {
            return readable;
        }

        public Gregorian getGregorian() {
            return gregorian;
        }
    }

    public static class Gregorian {
        @SerializedName("date")
        private String date;

        public String getDate() {
            return date;
        }
    }
} 
package uz.jtscorp.namoztime.data.remote;

import java.sql.Time;
import java.util.Date;

public class Timings {
    private Time Fajr;
    private Time Sunrise;
    private Time Dhuhr;
    private Time Asr;
    private Time Maghrib;
    private Time Isha;

    public Time getFajr() {
        return Fajr;
    }

    public void setFajr(Time fajr) {
        Fajr = fajr;
    }

    public Time getSunrise() {
        return Sunrise;
    }

    public void setSunrise(Time sunrise) {
        Sunrise = sunrise;
    }

    public Time getDhuhr() {
        return Dhuhr;
    }

    public void setDhuhr(Time dhuhr) {
        Dhuhr = dhuhr;
    }

    public Time getAsr() {
        return Asr;
    }

    public void setAsr(Time asr) {
        Asr = asr;
    }

    public Time getMaghrib() {
        return Maghrib;
    }

    public void setMaghrib(Time maghrib) {
        Maghrib = maghrib;
    }

    public Time getIsha() {
        return Isha;
    }

    public void setIsha(Time isha) {
        Isha = isha;
    }
}
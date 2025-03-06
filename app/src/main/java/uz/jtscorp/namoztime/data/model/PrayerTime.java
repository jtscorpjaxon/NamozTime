package uz.jtscorp.namoztime.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "prayer_times")
public class PrayerTime {
    @PrimaryKey
    @NonNull
    private String id;
    private Date date;
    private Time fajr;
    private Time sunrise;
    private Time dhuhr;
    private Time asr;
    private Time maghrib;
    private Time isha;
    private String location;

    public PrayerTime() {
        this.id = String.valueOf(new Date().getTime());
        this.location = "Olmaliq, Uzbekistan";
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getFajr() {
        return fajr;
    }

    public void setFajr(Time fajr) {
        this.fajr = fajr;
    }

    public Time getSunrise() {
        return sunrise;
    }

    public void setSunrise(Time sunrise) {
        this.sunrise = sunrise;
    }

    public Time getDhuhr() {
        return dhuhr;
    }

    public void setDhuhr(Time dhuhr) {
        this.dhuhr = dhuhr;
    }

    public Time getAsr() {
        return asr;
    }

    public void setAsr(Time asr) {
        this.asr = asr;
    }

    public Time getMaghrib() {
        return maghrib;
    }

    public void setMaghrib(Time maghrib) {
        this.maghrib = maghrib;
    }

    public Time getIsha() {
        return isha;
    }

    public void setIsha(Time isha) {
        this.isha = isha;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {

    }

    public void setTime(String time) {
    }

}
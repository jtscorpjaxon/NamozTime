package uz.jtscorp.namoztime.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "prayer_times")
public class PrayerTime {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String time;
    private String date;
    private boolean isNotified;
    private int notificationDelay; // minutlarda
    private boolean isSilentMode;
    private int silentModeDuration; // minutlarda

    public PrayerTime(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.isNotified = false;
        this.notificationDelay = 5; // default 5 minut
        this.isSilentMode = false;
        this.silentModeDuration = 15; // default 15 minut
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public int getNotificationDelay() {
        return notificationDelay;
    }

    public void setNotificationDelay(int notificationDelay) {
        this.notificationDelay = notificationDelay;
    }

    public boolean isSilentMode() {
        return isSilentMode;
    }

    public void setSilentMode(boolean silentMode) {
        isSilentMode = silentMode;
    }

    public int getSilentModeDuration() {
        return silentModeDuration;
    }

    public void setSilentModeDuration(int silentModeDuration) {
        this.silentModeDuration = silentModeDuration;
    }
} 
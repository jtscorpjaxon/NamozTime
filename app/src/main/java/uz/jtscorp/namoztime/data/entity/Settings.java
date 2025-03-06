package uz.jtscorp.namoztime.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "settings")
public class Settings {
    @PrimaryKey
    private int id;
    private boolean notificationsEnabled;
    private boolean silentModeEnabled;
    private boolean jumaSettingsEnabled;
    private String location; // Default: "Olmaliq"
    private double latitude;
    private double longitude;
    private int defaultNotificationDelay; // minutlarda
    private int defaultSilentModeDuration; // minutlarda
    private String jumaStartTime; // Default: "12:45"
    private String jumaEndTime; // Default: "13:20"

    public Settings() {
        this.id = 1; // Faqat bitta sozlamalar bo'ladi
        this.notificationsEnabled = true;
        this.silentModeEnabled = true;
        this.jumaSettingsEnabled = true;
        this.location = "Olmaliq";
        this.latitude = 40.8428; // Olmaliq koordinatalari
        this.longitude = 69.5975;
        this.defaultNotificationDelay = 5;
        this.defaultSilentModeDuration = 15;
        this.jumaStartTime = "12:45";
        this.jumaEndTime = "13:20";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public boolean isSilentModeEnabled() {
        return silentModeEnabled;
    }

    public void setSilentModeEnabled(boolean silentModeEnabled) {
        this.silentModeEnabled = silentModeEnabled;
    }

    public boolean isJumaSettingsEnabled() {
        return jumaSettingsEnabled;
    }

    public void setJumaSettingsEnabled(boolean jumaSettingsEnabled) {
        this.jumaSettingsEnabled = jumaSettingsEnabled;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getDefaultNotificationDelay() {
        return defaultNotificationDelay;
    }

    public void setDefaultNotificationDelay(int defaultNotificationDelay) {
        this.defaultNotificationDelay = defaultNotificationDelay;
    }

    public int getDefaultSilentModeDuration() {
        return defaultSilentModeDuration;
    }

    public void setDefaultSilentModeDuration(int defaultSilentModeDuration) {
        this.defaultSilentModeDuration = defaultSilentModeDuration;
    }

    public String getJumaStartTime() {
        return jumaStartTime;
    }

    public void setJumaStartTime(String jumaStartTime) {
        this.jumaStartTime = jumaStartTime;
    }

    public String getJumaEndTime() {
        return jumaEndTime;
    }

    public void setJumaEndTime(String jumaEndTime) {
        this.jumaEndTime = jumaEndTime;
    }
} 
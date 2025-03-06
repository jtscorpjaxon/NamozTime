package uz.jtscorp.namoztime.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_settings")
public class NotificationSettings {
    @PrimaryKey
    @NonNull
    private int id;
    private int fajrReminderMinutes;
    private int dhuhrReminderMinutes;
    private int asrReminderMinutes;
    private int maghribReminderMinutes;
    private int ishaReminderMinutes;
    private String jumaStartTime;
    private String jumaEndTime;
    private boolean isJumaEnabled;

    private boolean enabled;
    private boolean silentModeEnabled;

    public NotificationSettings() {
        this.id = 1;
        this.fajrReminderMinutes = 5;
        this.dhuhrReminderMinutes = 5;
        this.asrReminderMinutes = 5;
        this.maghribReminderMinutes = 5;
        this.ishaReminderMinutes = 5;
        this.jumaStartTime = "12:45";
        this.jumaEndTime = "13:20";
        this.isJumaEnabled = true;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public int getFajrReminderMinutes() {
        return fajrReminderMinutes;
    }

    public void setFajrReminderMinutes(int fajrReminderMinutes) {
        this.fajrReminderMinutes = fajrReminderMinutes;
    }

    public int getDhuhrReminderMinutes() {
        return dhuhrReminderMinutes;
    }

    public void setDhuhrReminderMinutes(int dhuhrReminderMinutes) {
        this.dhuhrReminderMinutes = dhuhrReminderMinutes;
    }

    public int getAsrReminderMinutes() {
        return asrReminderMinutes;
    }

    public void setAsrReminderMinutes(int asrReminderMinutes) {
        this.asrReminderMinutes = asrReminderMinutes;
    }

    public int getMaghribReminderMinutes() {
        return maghribReminderMinutes;
    }

    public void setMaghribReminderMinutes(int maghribReminderMinutes) {
        this.maghribReminderMinutes = maghribReminderMinutes;
    }

    public int getIshaReminderMinutes() {
        return ishaReminderMinutes;
    }

    public void setIshaReminderMinutes(int ishaReminderMinutes) {
        this.ishaReminderMinutes = ishaReminderMinutes;
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

    public boolean isJumaEnabled() {
        return isJumaEnabled;
    }

    public void setJumaEnabled(boolean jumaEnabled) {
        isJumaEnabled = jumaEnabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSilentModeEnabled() {
        return silentModeEnabled;
    }

    public void setSilentModeEnabled(boolean silentModeEnabled) {
        this.silentModeEnabled = silentModeEnabled;
    }
}
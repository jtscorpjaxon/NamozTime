package uz.jtscorp.namoztime.presentation;

import android.app.Application;
import android.content.Intent;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import uz.jtscorp.namoztime.data.entity.Settings;
import uz.jtscorp.namoztime.domain.repository.NotificationSettingsRepository;
import uz.jtscorp.namoztime.service.PrayerTimeService;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {
    private final NotificationSettingsRepository settingsRepository;

    @Inject
    public MainViewModel(
            Application application,
            NotificationSettingsRepository settingsRepository
    ) {
        super(application);
        this.settingsRepository = settingsRepository;
    }

    public void startPrayerTimeService() {
        Intent intent = new Intent(getApplication(), PrayerTimeService.class);
        getApplication().startService(intent);
    }

    public void stopPrayerTimeService() {
        Intent intent = new Intent(getApplication(), PrayerTimeService.class);
        getApplication().stopService(intent);
    }

    public LiveData<Settings> getSettings() {
        return settingsRepository.getSettings();
    }

    public void updateNotificationsEnabled(boolean enabled) {
        settingsRepository.updateNotificationsEnabled(enabled);
    }

    public void updateSilentModeEnabled(boolean enabled) {
        settingsRepository.updateSilentModeEnabled(enabled);
    }

    public void updateJumaSettingsEnabled(boolean enabled) {
        settingsRepository.updateJumaSettingsEnabled(enabled);
    }

    public void updateLocation(String location, double latitude, double longitude) {
        settingsRepository.updateLocation(location, latitude, longitude);
    }

    public void updateDefaultNotificationDelay(int delay) {
        settingsRepository.updateDefaultNotificationDelay(delay);
    }

    public void updateDefaultSilentModeDuration(int duration) {
        settingsRepository.updateDefaultSilentModeDuration(duration);
    }

    public void updateJumaTimes(String startTime, String endTime) {
        settingsRepository.updateJumaTimes(startTime, endTime);
    }
}
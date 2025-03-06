package uz.jtscorp.namoztime.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import uz.jtscorp.namoztime.data.model.NotificationSettings;
import uz.jtscorp.namoztime.domain.repository.NotificationSettingsRepository;
import uz.jtscorp.namoztime.service.PrayerTimeService;

@HiltViewModel
public class MainViewModel extends ViewModel {
    private final PrayerTimeService prayerTimeService;
    private final NotificationSettingsRepository notificationSettingsRepository;

    @Inject
    public MainViewModel(
            PrayerTimeService prayerTimeService,
            NotificationSettingsRepository notificationSettingsRepository
    ) {
        this.prayerTimeService = prayerTimeService;
        this.notificationSettingsRepository = notificationSettingsRepository;
    }

    public String getCurrentPrayerTime() {
        return prayerTimeService.getCurrentPrayerTime();
    }


    public LiveData<NotificationSettings> getNotificationSettings() {
        return notificationSettingsRepository.getSettings();
    }

    public void updatePrayerTimes(double latitude, double longitude) {
        prayerTimeService.updatePrayerTimes(latitude, longitude);
    }

    public void updateNotificationEnabled(boolean enabled) {
        NotificationSettings settings = notificationSettingsRepository.getSettings().getValue();
        if (settings != null) {
            settings.setEnabled(enabled);
            notificationSettingsRepository.updateSettings(settings);
        }
    }

    public void updateSilentModeEnabled(boolean enabled) {
        NotificationSettings settings = notificationSettingsRepository.getSettings().getValue();
        if (settings != null) {
            settings.setSilentModeEnabled(enabled);
            notificationSettingsRepository.updateSettings(settings);
        }
    }

    public void updateJumaSettingsEnabled(boolean enabled) {
        NotificationSettings settings = notificationSettingsRepository.getSettings().getValue();
        if (settings != null) {
            settings.setJumaEnabled(enabled);
            notificationSettingsRepository.updateSettings(settings);
        }
    }

    public void updateReminderMinutes(String prayerName, int minutes) {
        NotificationSettings settings = notificationSettingsRepository.getSettings().getValue();
        if (settings != null) {
            switch (prayerName) {
                case "Bomdod":
                    settings.setFajrReminderMinutes(minutes);
                    break;
                case "Peshin":
                    settings.setDhuhrReminderMinutes(minutes);
                    break;
                case "Asr":
                    settings.setAsrReminderMinutes(minutes);
                    break;
                case "Shom":
                    settings.setMaghribReminderMinutes(minutes);
                    break;
                case "Xufton":
                    settings.setIshaReminderMinutes(minutes);
                    break;
            }
            notificationSettingsRepository.updateSettings(settings);
        }
    }

    public void updateJumaTime(String startTime, String endTime) {
        NotificationSettings settings = notificationSettingsRepository.getSettings().getValue();
        if (settings != null) {
            settings.setJumaStartTime(startTime);
            settings.setJumaEndTime(endTime);
            notificationSettingsRepository.updateSettings(settings);
        }
    }

    public String getNextPrayerTime(String currentPrayerTime) {
        return prayerTimeService.getNextPrayTime(currentPrayerTime);
    }
}
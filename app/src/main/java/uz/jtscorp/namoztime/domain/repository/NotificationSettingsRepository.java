package uz.jtscorp.namoztime.domain.repository;

import androidx.lifecycle.LiveData;

import uz.jtscorp.namoztime.data.model.NotificationSettings;

public interface NotificationSettingsRepository {
    LiveData<NotificationSettings> getSettings();
    void updateSettings(NotificationSettings settings);
    void insertSettings(NotificationSettings settings);
} 
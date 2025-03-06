package uz.jtscorp.namoztime.data.repository;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;

import uz.jtscorp.namoztime.data.local.NotificationSettingsDao;
import uz.jtscorp.namoztime.data.model.NotificationSettings;
import uz.jtscorp.namoztime.domain.repository.NotificationSettingsRepository;

public class NotificationSettingsRepositoryImpl implements NotificationSettingsRepository {
    private final NotificationSettingsDao dao;

    @Inject
    public NotificationSettingsRepositoryImpl(NotificationSettingsDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<NotificationSettings> getSettings() {
        return dao.getSettings();
    }

    @Override
    public void updateSettings(NotificationSettings settings) {
        dao.update(settings);
    }

    @Override
    public void insertSettings(NotificationSettings settings) {
        dao.insert(settings);
    }
} 
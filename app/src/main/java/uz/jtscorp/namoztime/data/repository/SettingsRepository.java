package uz.jtscorp.namoztime.data.repository;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import uz.jtscorp.namoztime.data.dao.SettingsDao;
import uz.jtscorp.namoztime.data.entity.Settings;

@Singleton
public class SettingsRepository {
    private final SettingsDao dao;

    @Inject
    public SettingsRepository(SettingsDao dao) {
        this.dao = dao;
    }

    public LiveData<Settings> getSettings() {
        return dao.getSettings();
    }

    public Single<Settings> getSettingsSync() {
        return Single.fromCallable(dao::getSettingsSync)
                .subscribeOn(Schedulers.io());
    }

    public Completable updateNotificationsEnabled(boolean enabled) {
        return Completable.fromAction(() -> dao.updateNotificationsEnabled(enabled))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateSilentModeEnabled(boolean enabled) {
        return Completable.fromAction(() -> dao.updateSilentModeEnabled(enabled))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateJumaSettingsEnabled(boolean enabled) {
        return Completable.fromAction(() -> dao.updateJumaSettingsEnabled(enabled))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateLocation(String location, double latitude, double longitude) {
        return Completable.fromAction(() -> dao.updateLocation(location, latitude, longitude))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateDefaultNotificationDelay(int delay) {
        return Completable.fromAction(() -> dao.updateDefaultNotificationDelay(delay))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateDefaultSilentModeDuration(int duration) {
        return Completable.fromAction(() -> dao.updateDefaultSilentModeDuration(duration))
                .subscribeOn(Schedulers.io());
    }

    public Completable updateJumaTimes(String startTime, String endTime) {
        return Completable.fromAction(() -> dao.updateJumaTimes(startTime, endTime))
                .subscribeOn(Schedulers.io());
    }
} 
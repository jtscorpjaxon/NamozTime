package uz.jtscorp.namoztime.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import uz.jtscorp.namoztime.data.entity.Settings;
import uz.jtscorp.namoztime.data.repository.SettingsRepository;
import uz.jtscorp.namoztime.util.AudioHelper;

@HiltViewModel
public class SettingsViewModel extends ViewModel {
    private final SettingsRepository settingsRepository;
    private final AudioHelper audioHelper;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(
            SettingsRepository settingsRepository,
            AudioHelper audioHelper) {
        this.settingsRepository = settingsRepository;
        this.audioHelper = audioHelper;
    }

    public LiveData<Settings> getSettings() {
        return settingsRepository.getSettings();
    }

    public void updateNotificationsEnabled(boolean enabled) {
        disposables.add(
            settingsRepository.updateNotificationsEnabled(enabled)
                .subscribe(
                    () -> error.postValue(null),
                    throwable -> error.postValue(throwable.getMessage())
                )
        );
    }

    public void updateSilentModeEnabled(boolean enabled) {
        if (enabled && !audioHelper.hasNotificationPolicyAccess()) {
            error.setValue("Ovozsiz rejim uchun ruxsat kerak");
            return;
        }

        disposables.add(
            settingsRepository.updateSilentModeEnabled(enabled)
                .subscribe(
                    () -> error.postValue(null),
                    throwable -> error.postValue(throwable.getMessage())
                )
        );
    }

    public void updateJumaSettingsEnabled(boolean enabled) {
        disposables.add(
            settingsRepository.updateJumaSettingsEnabled(enabled)
                .subscribe(
                    () -> error.postValue(null),
                    throwable -> error.postValue(throwable.getMessage())
                )
        );
    }

    public void updateLocation(String location, double latitude, double longitude) {
        disposables.add(
            settingsRepository.updateLocation(location, latitude, longitude)
                .subscribe(
                    () -> error.postValue(null),
                    throwable -> error.postValue(throwable.getMessage())
                )
        );
    }

    public void updateDefaultNotificationDelay(int delay) {
        disposables.add(
            settingsRepository.updateDefaultNotificationDelay(delay)
                .subscribe(
                    () -> error.postValue(null),
                    throwable -> error.postValue(throwable.getMessage())
                )
        );
    }

    public void updateDefaultSilentModeDuration(int duration) {
        disposables.add(
            settingsRepository.updateDefaultSilentModeDuration(duration)
                .subscribe(
                    () -> error.postValue(null),
                    throwable -> error.postValue(throwable.getMessage())
                )
        );
    }

    public void updateJumaTimes(String startTime, String endTime) {
        disposables.add(
            settingsRepository.updateJumaTimes(startTime, endTime)
                .subscribe(
                    () -> error.postValue(null),
                    throwable -> error.postValue(throwable.getMessage())
                )
        );
    }

    public boolean hasNotificationPolicyAccess() {
        return audioHelper.hasNotificationPolicyAccess();
    }

    public LiveData<String> getError() {
        return error;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 
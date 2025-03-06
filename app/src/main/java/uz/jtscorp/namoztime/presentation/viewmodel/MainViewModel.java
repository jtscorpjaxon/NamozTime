package uz.jtscorp.namoztime.presentation.viewmodel;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import uz.jtscorp.namoztime.data.entity.Settings;
import uz.jtscorp.namoztime.data.repository.SettingsRepository;
import uz.jtscorp.namoztime.util.AudioHelper;

@HiltViewModel
public class MainViewModel extends AndroidViewModel {
    private final SettingsRepository settingsRepository;
    private final AudioHelper audioHelper;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<Boolean> permissionsGranted = new MutableLiveData<>(false);

    @Inject
    public MainViewModel(
            Application application,
            SettingsRepository settingsRepository,
            AudioHelper audioHelper) {
        super(application);
        this.settingsRepository = settingsRepository;
        this.audioHelper = audioHelper;
        checkPermissions();
    }

    private void checkPermissions() {
        boolean locationPermissionGranted = ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean notificationPermissionGranted = ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED;

        boolean notificationPolicyAccessGranted = audioHelper.hasNotificationPolicyAccess();

        permissionsGranted.setValue(
                locationPermissionGranted &&
                notificationPermissionGranted &&
                notificationPolicyAccessGranted
        );
    }

    public LiveData<Boolean> getPermissionsGranted() {
        return permissionsGranted;
    }

    public LiveData<Settings> getSettings() {
        return settingsRepository.getSettings();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
} 
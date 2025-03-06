package uz.jtscorp.namoztime.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import uz.jtscorp.namoztime.data.entity.PrayerTime;
import uz.jtscorp.namoztime.data.repository.PrayerTimeRepository;
import uz.jtscorp.namoztime.data.repository.SettingsRepository;

@HiltViewModel
public class PrayerTimesViewModel extends ViewModel {
    private final PrayerTimeRepository prayerTimeRepository;
    private final SettingsRepository settingsRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    @Inject
    public PrayerTimesViewModel(
            PrayerTimeRepository prayerTimeRepository,
            SettingsRepository settingsRepository) {
        this.prayerTimeRepository = prayerTimeRepository;
        this.settingsRepository = settingsRepository;
        updatePrayerTimes();
    }

    private void updatePrayerTimes() {
        isLoading.setValue(true);
        disposables.add(
            settingsRepository.getSettingsSync()
                .flatMapCompletable(settings ->
                    prayerTimeRepository.updatePrayerTimes(
                        settings.getLatitude(),
                        settings.getLongitude()
                    )
                )
                .subscribe(
                    () -> {
                        isLoading.postValue(false);
                        error.postValue(null);
                    },
                    throwable -> {
                        isLoading.postValue(false);
                        error.postValue(throwable.getMessage());
                    }
                )
        );
    }

    public LiveData<List<PrayerTime>> getPrayerTimesForToday() {
        String today = dateFormat.format(new Date());
        return prayerTimeRepository.getPrayerTimesForDate(today);
    }

    public LiveData<PrayerTime> getCurrentPrayerTime() {
        String today = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        return prayerTimeRepository.getCurrentPrayerTime(today, currentTime);
    }

    public LiveData<PrayerTime> getNextPrayerTime() {
        String today = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        return prayerTimeRepository.getNextPrayerTime(today, currentTime);
    }

    public void refreshPrayerTimes() {
        updatePrayerTimes();
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
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
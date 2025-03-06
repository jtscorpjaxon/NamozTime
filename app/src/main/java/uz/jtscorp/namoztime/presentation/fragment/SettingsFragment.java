package uz.jtscorp.namoztime.presentation.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.databinding.FragmentSettingsBinding;
import uz.jtscorp.namoztime.presentation.viewmodel.SettingsViewModel;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private SettingsViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        setupObservers();
    }

    private void setupUI() {
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.updateNotificationsEnabled(isChecked));

        binding.switchSilentMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !viewModel.hasNotificationPolicyAccess()) {
                buttonView.setChecked(false);
                startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
                return;
            }
            viewModel.updateSilentModeEnabled(isChecked);
        });

        binding.switchJumaSettings.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.updateJumaSettingsEnabled(isChecked));

        binding.btnJumaStartTime.setOnClickListener(v -> showTimePickerDialog(true));
        binding.btnJumaEndTime.setOnClickListener(v -> showTimePickerDialog(false));

        binding.sliderNotificationDelay.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                viewModel.updateDefaultNotificationDelay((int) value);
            }
        });

        binding.sliderSilentModeDuration.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                viewModel.updateDefaultSilentModeDuration((int) value);
            }
        });

        binding.btnLocationSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });
    }

    private void setupObservers() {
        viewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
            binding.switchNotifications.setChecked(settings.isNotificationsEnabled());
            binding.switchSilentMode.setChecked(settings.isSilentModeEnabled());
            binding.switchJumaSettings.setChecked(settings.isJumaSettingsEnabled());
            binding.sliderNotificationDelay.setValue(settings.getDefaultNotificationDelay());
            binding.sliderSilentModeDuration.setValue(settings.getDefaultSilentModeDuration());
            binding.tvLocation.setText(settings.getLocation());
            binding.btnJumaStartTime.setText(settings.getJumaStartTime());
            binding.btnJumaEndTime.setText(settings.getJumaEndTime());
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void showTimePickerDialog(boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, selectedMinute) -> {
                    String time = String.format("%02d:%02d", hourOfDay, selectedMinute);
                    if (isStartTime) {
                        binding.btnJumaStartTime.setText(time);
                    } else {
                        binding.btnJumaEndTime.setText(time);
                    }
                    viewModel.updateJumaTimes(
                            binding.btnJumaStartTime.getText().toString(),
                            binding.btnJumaEndTime.getText().toString()
                    );
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 
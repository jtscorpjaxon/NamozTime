package uz.jtscorp.namoztime.presentation.fragment;

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

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.databinding.FragmentSettingsBinding;
import uz.jtscorp.namoztime.presentation.MainViewModel;

@AndroidEntryPoint
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
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
    }

    private void setupUI() {
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.updateNotificationEnabled(isChecked));

        binding.switchSilentMode.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.updateSilentModeEnabled(isChecked));

        binding.switchJumaSettings.setOnCheckedChangeListener((buttonView, isChecked) ->
                viewModel.updateJumaSettingsEnabled(isChecked));

        binding.btnWifiSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        });

        binding.btnMobileDataSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
            startActivity(intent);
        });

        binding.btnLocationSettings.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });

        // Load saved settings
        viewModel.getNotificationEnabled().observe(getViewLifecycleOwner(),
                enabled -> binding.switchNotifications.setChecked(enabled));

        viewModel.getSilentModeEnabled().observe(getViewLifecycleOwner(),
                enabled -> binding.switchSilentMode.setChecked(enabled));

        viewModel.getJumaSettingsEnabled().observe(getViewLifecycleOwner(),
                enabled -> binding.switchJumaSettings.setChecked(enabled));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 
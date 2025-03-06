package uz.jtscorp.namoztime.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.sql.Time;

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.data.model.PrayerTime;
import uz.jtscorp.namoztime.databinding.ActivityMainBinding;
import uz.jtscorp.namoztime.service.PrayerTimeService;
import uz.jtscorp.namoztime.worker.PrayerTimeWorker;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        checkPermissions();
        setupUI();
        startServices();
    }

    private void checkPermissions() {
        String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        };

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }

        // Check notification policy access
        if (!isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    private boolean isNotificationPolicyAccessGranted() {
        return getSystemService(android.app.NotificationManager.class).isNotificationPolicyAccessGranted();
    }

    private void setupUI() {
        // Setup switches
        binding.switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.updateNotificationEnabled(isChecked);
        });

        binding.switchSilentMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.updateSilentModeEnabled(isChecked);
        });

        binding.switchJumaSettings.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.updateJumaSettingsEnabled(isChecked);
        });

        // Setup buttons
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
        String currentPrayerTime = viewModel.getCurrentPrayerTime();
        binding.tvCurrentPrayer.setText(String.format("Joriy namoz: %s", currentPrayerTime));
        binding.tvCurrentPrayer.setText(String.format("Keyingi namoz: %s", viewModel.getNextPrayerTime(currentPrayerTime)));
    }

    private void startServices() {
        // Schedule WorkManager
        PrayerTimeWorker.schedule(this);

        // Start foreground service
        Intent serviceIntent = new Intent(this, PrayerTimeService.class);
        //check SDK 26
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) {
                Toast.makeText(this, "Barcha ruxsatlar kerak", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
} 
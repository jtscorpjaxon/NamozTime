package uz.jtscorp.namoztime.presentation.fragment;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.databinding.FragmentQiblaBinding;
import uz.jtscorp.namoztime.presentation.viewmodel.SettingsViewModel;
import uz.jtscorp.namoztime.util.QiblaCalculator;

import static android.content.Context.SENSOR_SERVICE;

@AndroidEntryPoint
public class QiblaFragment extends Fragment implements SensorEventListener {
    private FragmentQiblaBinding binding;
    private SettingsViewModel viewModel;
    private SensorManager sensorManager;
    private float currentDegree = 0f;
    private float qiblaDegree = 0f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQiblaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getSettings().observe(getViewLifecycleOwner(), settings -> {
            qiblaDegree = QiblaCalculator.calculateQiblaDirection(
                settings.getLatitude(),
                settings.getLongitude()
            );
            binding.tvLocation.setText(settings.getLocation());
            binding.tvQiblaDegree.setText(String.format("Qibla: %.1f°", qiblaDegree));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor compass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (compass != null) {
            sensorManager.registerListener(this, compass, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = Math.round(event.values[0]);
        float rotation = degree + qiblaDegree;

        RotateAnimation rotateAnimation = new RotateAnimation(
                currentDegree,
                -rotation,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);

        binding.ivCompass.startAnimation(rotateAnimation);
        currentDegree = -rotation;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 
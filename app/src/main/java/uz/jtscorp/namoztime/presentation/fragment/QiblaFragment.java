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

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.databinding.FragmentQiblaBinding;

import static android.content.Context.SENSOR_SERVICE;

@AndroidEntryPoint
public class QiblaFragment extends Fragment implements SensorEventListener {
    private FragmentQiblaBinding binding;
    private SensorManager sensorManager;
    private float currentDegree = 0f;
    private static final float QIBLA_DEGREE = 11.7f; // Olmaliq uchun qibla yo'nalishi

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQiblaBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
        float rotation = degree + QIBLA_DEGREE;

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
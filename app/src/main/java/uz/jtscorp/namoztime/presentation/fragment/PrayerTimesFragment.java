package uz.jtscorp.namoztime.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.databinding.FragmentPrayerTimesBinding;
import uz.jtscorp.namoztime.presentation.MainViewModel;

@AndroidEntryPoint
public class PrayerTimesFragment extends Fragment {
    private FragmentPrayerTimesBinding binding;
    private MainViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrayerTimesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObservers();
    }

    private void setupObservers() {
        viewModel.getCurrentPrayerTime().observe(getViewLifecycleOwner(), prayerTime -> {
            if (prayerTime != null) {
                binding.tvCurrentPrayer.setText(prayerTime.getName() + ": " + prayerTime.getTime());
            }
        });

        viewModel.getNextPrayerTime().observe(getViewLifecycleOwner(), prayerTime -> {
            if (prayerTime != null) {
                binding.tvNextPrayer.setText("Keyingi namoz: " + prayerTime.getName() + " - " + prayerTime.getTime());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 
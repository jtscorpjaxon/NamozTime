package uz.jtscorp.namoztime.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.databinding.FragmentPrayerTimesBinding;
import uz.jtscorp.namoztime.presentation.adapter.PrayerTimesAdapter;
import uz.jtscorp.namoztime.presentation.viewmodel.PrayerTimesViewModel;

@AndroidEntryPoint
public class PrayerTimesFragment extends Fragment {
    private FragmentPrayerTimesBinding binding;
    private PrayerTimesViewModel viewModel;
    private PrayerTimesAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PrayerTimesViewModel.class);
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
        setupRecyclerView();
      //  setupSwipeRefresh();
        setupObservers();
    }

    private void setupRecyclerView() {
        adapter = new PrayerTimesAdapter();
        binding.rvPrayerTimes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPrayerTimes.setAdapter(adapter);
    }

    private void setupSwipeRefresh() {
     //   binding.swipeRefresh.setOnRefreshListener(() -> viewModel.refreshPrayerTimes());
    }

    private void setupObservers() {
        viewModel.getPrayerTimesForToday().observe(getViewLifecycleOwner(), prayerTimes -> {
            adapter.submitList(prayerTimes);
        });

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

//        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
//            binding.swipeRefresh.setRefreshing(isLoading);
//        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Snackbar.make(binding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 
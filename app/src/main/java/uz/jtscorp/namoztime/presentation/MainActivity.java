package uz.jtscorp.namoztime.presentation;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import dagger.hilt.android.AndroidEntryPoint;
import uz.jtscorp.namoztime.R;
import uz.jtscorp.namoztime.databinding.ActivityMainBinding;
import uz.jtscorp.namoztime.presentation.viewmodel.PrayerTimesViewModel;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private PrayerTimesViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(PrayerTimesViewModel.class);

        setupNavigation();
        setSupportActionBar(binding.topAppBar);
    }
    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment topilmadi. activity_main.xml ni tekshiring.");
            throw new IllegalStateException("NavHostFragment topilmadi!");
        }

        navController = navHostFragment.getNavController();
        if (navController == null) {
            Log.e("MainActivity", "NavController null bo‘lib qoldi. nav_graph.xml ni tekshiring.");
            throw new IllegalStateException("NavController null!");
        }

        if (binding.bottomNavigation == null) {
            Log.e("MainActivity", "BottomNavigationView null. activity_main.xml ni tekshiring.");
            throw new IllegalStateException("BottomNavigationView topilmadi!");
        }

        NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
} 
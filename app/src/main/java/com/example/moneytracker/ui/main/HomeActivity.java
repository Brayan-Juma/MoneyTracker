package com.example.moneytracker.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.moneytracker.R;
import com.example.moneytracker.ui.dashboard.DashboardFragment;
import com.example.moneytracker.ui.transactions.TransactionsFragment;
import com.example.moneytracker.ui.stats.StatsFragment;
import com.example.moneytracker.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNav = findViewById(R.id.bottomNav);

        loadFragment(new DashboardFragment()); // pantalla inicial

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                loadFragment(new DashboardFragment());
            }
            else if (id == R.id.nav_transactions) {
                loadFragment(new TransactionsFragment());
            }
            else if (id == R.id.nav_stats) {
                loadFragment(new StatsFragment());
            }
            else if (id == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
            }

            return true;
        });

    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.fade_in,   // animación al entrar
                            R.anim.fade_out   // animación al salir
                    )
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}

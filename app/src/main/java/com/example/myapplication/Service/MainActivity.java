package com.example.myapplication.Service;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    static HomeFragment homeFragment = new HomeFragment();
    static SearchFragment searchFragment = new SearchFragment();
    static SettingsFragment settingFragment = new SettingsFragment();
    static UserProfileFragment userProfileFragment = new UserProfileFragment();

    static LoginActivity loginFragment = new LoginActivity();
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.container);

        // Tạo adapter cho ViewPager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Đặt listener cho BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.search:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.user_profile:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.setting:
                        viewPager.setCurrentItem(3);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, settingFragment)
                                .commit();
                        return true;
                    default:
                        return false;
                }
            }
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isDarkModeEnabled = sharedPreferences.getBoolean("dark_mode_switch", false);
        setAppTheme(isDarkModeEnabled);
    }

    private void setAppTheme(boolean isDarkModeEnabled) {
        if (isDarkModeEnabled) {
            setTheme(R.style.AppTheme_Dark); // Áp dụng theme dark
        } else {
            setTheme(R.style.AppTheme_Light); // Áp dụng theme sáng
        }
    }


    private static class PagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 4;

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return homeFragment;
                case 1:
                    return searchFragment;
                case 2:
                    return userProfileFragment;
                case 3:
                    return settingFragment;
                default:
                    return null;
            }
        }
    }
    public void onThemeChanged(boolean isDarkModeEnabled) {
        setTheme(isDarkModeEnabled ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
        recreate(); // Tái tạo Activity để áp dụng theme mới
    }
}

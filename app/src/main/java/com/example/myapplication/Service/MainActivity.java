package com.example.myapplication.Service;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceFragmentCompat;
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
                        return true;
                    default:
                        return false;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Không cần thực hiện gì trong phương thức này
            }

            @Override
            public void onPageSelected(int position) {
                // Cập nhật item được chọn trong BottomNavigationView khi vuốt màn hình
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Không cần thực hiện gì trong phương thức này
            }
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals("dark_mode_switch")) {
                boolean darkModeEnabled = sharedPreferences.getBoolean(key, false);
                if (darkModeEnabled) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                getActivity().recreate();
            }
        }
    }


    private static class PagerAdapter extends FragmentPagerAdapter {
        private static final int NUM_PAGES = 4;

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
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
}
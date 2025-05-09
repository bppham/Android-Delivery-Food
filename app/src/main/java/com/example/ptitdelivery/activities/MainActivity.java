package com.example.ptitdelivery.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ptitdelivery.R;
import com.example.ptitdelivery.viewPagerAdapters.ViewPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private TextView toolbarTitle;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbarTitle = findViewById(R.id.toolbar_title);
        mViewPager = findViewById(R.id.view_pager);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(0);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbarTitle.setText("Đơn hàng");
                        mBottomNavigationView.getMenu().findItem(R.id.nav_orders).setChecked(true);
                        break;
                    case 1:
                        toolbarTitle.setText("Tổng kết");
                        mBottomNavigationView.getMenu().findItem(R.id.nav_summary).setChecked(true);
                        break;
                    case 2:
                        toolbarTitle.setText("Tài khoản");
                        mBottomNavigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        break;
                    case 3:
                        toolbarTitle.setText("Trò chuyện");
                        mBottomNavigationView.getMenu().findItem(R.id.nav_policy).setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_orders:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_summary:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_profile:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_policy:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}
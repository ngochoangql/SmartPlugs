package com.example.artermis2.Screen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.artermis2.Adapter.MyPagerAdapter;
import com.example.artermis2.Component.IMyActionBar;
import com.example.artermis2.Component.MyActionBar;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Fragment.Edit;
import com.example.artermis2.Fragment.Limit;
import com.example.artermis2.Fragment.Overview;
import com.example.artermis2.Fragment.Scheduler;
import com.example.artermis2.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class DeviceMenuScreen extends AppCompatActivity implements IMyActionBar {

    TextView overview,scheduler,limit,edit;
    String fragmentPage;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    public void Init(){
//        overview = findViewById(R.id.overviewId);
//        scheduler = findViewById(R.id.schedulerId);
//        limit = findViewById(R.id.limitId);
//        edit = findViewById(R.id.edit);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_menu_screen);
        MyActionBar myActionBar = new MyActionBar(this, DataHolder.getDevice().device_name);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.deviceActionBar, myActionBar)
                .commit();

        viewPager = findViewById(R.id.container_device);
        tabLayout = findViewById(R.id.tabLayout);
//        tabLayout.setLayoutParams(new LinearLayout.LayoutParams(120,50));
        // Tạo adapter cho ViewPager2

        MyPagerAdapter pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        // Kết nối TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {

//                    tab.getCustomView().setLayoutParams(new LinearLayout.LayoutParams(120 ,50));
//                    tab.setText(getTabTitle(position));
                    View customView = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null);
                    TextView textView = customView.findViewById(R.id.tabText);
                    textView.setText(getTabTitle(position));
                    tab.setCustomView(customView);
                }
        ).attach();

        fragmentPage = getIntent().getStringExtra("fragment_container");
        if (fragmentPage.equals("scheduler")){
            TabLayout.Tab selectedTab = tabLayout.getTabAt(1); // Vị trí của tab cần chọn

            if (selectedTab != null) {
                selectedTab.select();
            }
        }
        HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Tính toán vị trí của tab được chọn
                TabLayout.Tab tab = tabLayout.getTabAt(position);
//                View customView = LayoutInflater.from(DeviceMenuScreen.this).inflate(R.layout.custom_tab_layout, null);
//                TextView textView = customView.findViewById(R.id.tabText);
                int scrollX = tab != null ? tab.getPosition() * tab.getCustomView().getWidth() : 0;

                // Cuộn HorizontalScrollView đến vị trí của tab được chọn
                horizontalScrollView.scrollTo(scrollX, 0);
            }
        });



    }
    private String getTabTitle(int position) {
        switch (position) {
            case 0:
                return "Tổng quan";
            case 1:
                return "Thống kê";
            case 2:
                return "Hoạt động";
            case 3:
                return "Giới hạn";
            case 4:
                return "Chỉnh sửa";
            case 5:
                return "Lịch sử";
            default:
                return "Tab " + (position + 1);
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }
    @Override
    public void onBackButton() {
        super.onBackPressed();
    }

    @Override
    public void onHomeButton() {

    }
    @Override
    public void onResume() {
        super.onResume();
//
//        if (fragmentPage.equals("scheduler")){
//            TabLayout.Tab selectedTab = tabLayout.getTabAt(1); // Vị trí của tab cần chọn
//
//            if (selectedTab != null) {
//                selectedTab.select();
//            }
//        }

    }
}
package com.example.testdemo.viewpager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.testdemo.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerDemoActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_demo);
        viewPager = findViewById(R.id.viewpager);
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataList.add("" + i);
        }
        viewPager.setOffscreenPageLimit(dataList.size());
        viewPager.setPageTransformer(false, new DepthPagerTransformer());
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(dataList);
        viewPager.setAdapter(pagerAdapter);
        int initPosition = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % dataList.size();
        viewPager.setCurrentItem(initPosition);
    }
}
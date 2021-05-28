package com.example.testdemo.ring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testdemo.R;

public class RingTestActivity extends AppCompatActivity {

    CircleRingProgressView circleRingProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_test);
        circleRingProgressView = findViewById(R.id.progress_view);
        circleRingProgressView.refresh(50);
    }
}
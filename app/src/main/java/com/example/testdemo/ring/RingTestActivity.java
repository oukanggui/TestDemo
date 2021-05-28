package com.example.testdemo.ring;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testdemo.R;

public class RingTestActivity extends AppCompatActivity {

    CircleRingProgressView circleRingProgressView;

    ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_test);
        circleRingProgressView = findViewById(R.id.progress_view);
        circleRingProgressView.refresh(50);
        colorPickerView = findViewById(R.id.color_picker);
        circleRingProgressView.setProgressColor(Color.rgb(255, 0, 0));
        colorPickerView.setOnColorPickerChangeListener(new ColorPickerView.OnColorPickerChangeListener() {
            @Override
            public void onColorChanged(ColorPickerView picker, int color) {
                circleRingProgressView.setProgressColor(color);
            }

            @Override
            public void onStartTrackingTouch(ColorPickerView picker) {

            }

            @Override
            public void onStopTrackingTouch(ColorPickerView picker) {

            }
        });
    }
}
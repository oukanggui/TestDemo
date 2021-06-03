package com.example.testdemo.ring;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.testdemo.R;

public class ProgressCircleImageActivity extends AppCompatActivity {
    private ProgressCircleImageLayout progressCircleImageLayout;
    ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_circle_image);
        colorPickerView = findViewById(R.id.color_picker);
        progressCircleImageLayout = findViewById(R.id.layout_progress_circle_image);
        //progressCircleImageLayout.setProgressColor(Color.rgb(255, 0, 0));
        progressCircleImageLayout.refresh(80);
        colorPickerView.setOnColorPickerChangeListener(new ColorPickerView.OnColorPickerChangeListener() {
            @Override
            public void onColorChanged(ColorPickerView picker, int color) {
                progressCircleImageLayout.setProgressColor(color);
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
package com.example.testdemo.segment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.testdemo.R;
import com.example.testdemo.segment.view.SegmentedControlItem;
import com.example.testdemo.segment.view.SegmentedControlView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SegmentTestActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private SegmentedControlView mScv1;
    private SegmentedControlView mScv2;
    private SegmentedControlView mScv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_test);

        mScv1 = (SegmentedControlView)findViewById(R.id.scv1);
        mScv2 = (SegmentedControlView)findViewById(R.id.scv2);
        mScv3 = (SegmentedControlView)findViewById(R.id.scv3);

        List<SegmentedControlItem> items = new ArrayList<>();
        items.add(new SegmentedControlItem("昨天"));
        items.add(new SegmentedControlItem("今天"));
        items.add(new SegmentedControlItem("明天"));
        mScv1.addItems(items);

        mScv2.addItems(items);
        mScv3.addItems(items);

        mScv1.setOnSegItemClickListener(new SegmentedControlView.OnSegItemClickListener() {
            @Override
            public void onItemClick(SegmentedControlItem item, int position) {
                String msg = String.format(Locale.getDefault(), "click scv1 selected:%d", position);
                Log.d(TAG, msg);
            }
        });

        mScv2.setOnSegItemClickListener(new SegmentedControlView.OnSegItemClickListener() {
            @Override
            public void onItemClick(SegmentedControlItem item, int position) {
                String msg = String.format(Locale.getDefault(), "click scv2 selected:%d", position);
                Log.d(TAG, msg);
            }
        });

        mScv3.setOnSegItemClickListener(new SegmentedControlView.OnSegItemClickListener() {
            @Override
            public void onItemClick(SegmentedControlItem item, int position) {
                String msg = String.format(Locale.getDefault(), "click scv3 selected:%d", position);
                Log.d(TAG, msg);
            }
        });
    }
}
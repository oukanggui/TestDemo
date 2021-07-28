package com.example.testdemo.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testdemo.R;

public class FloatViewActivity extends AppCompatActivity {
    private FloatingWindowHelper floatingWindowHelper;
    private Button btnShow, btnHide;
    private MusicFloatLayout musicFloatLayout;
    private View layoutExpand, viewDrag;
    private View floatWindowDeleteView;
    private TextView tvDelete;
    private boolean isOpen = false;
    private int widthExpand;
    private int widthShrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_view);
        FloatingWindowHelper.canDrawOverlays(this, true);
        floatingWindowHelper = new FloatingWindowHelper(this);
        View musicRootLayout = LayoutInflater.from(this).inflate(R.layout.layout_float_window, null, false);
        musicFloatLayout = musicRootLayout.findViewById(R.id.layout_root);
        viewDrag = musicRootLayout.findViewById(R.id.view_drag);
        layoutExpand = musicRootLayout.findViewById(R.id.layout_expand);
        floatWindowDeleteView = LayoutInflater.from(this).inflate(R.layout.layout_float_window_delete, null, false);
        tvDelete = floatWindowDeleteView.findViewById(R.id.tv_delete);
        btnShow = findViewById(R.id.btn_show);
        btnHide = findViewById(R.id.btn_hide);
        widthExpand = dp2px(this, 220);
        widthShrink = dp2px(this, 100);
        musicFloatLayout.setOnCustomClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = !isOpen;
                if (isOpen) {
                    layoutExpand.setVisibility(View.VISIBLE);
                    musicFloatLayout.alignToWindowRight(widthExpand);
                } else {
                    layoutExpand.setVisibility(View.GONE);
                    musicFloatLayout.alignToWindowRight(widthShrink);
                }
            }
        });
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicFloatLayout.show(new MusicFloatLayout.OnDragListener() {
                    @Override
                    public void onDragStart() {

                    }

                    @Override
                    public void onDragging(boolean isReachDeleteArea) {
                        Log.d("CBaymax", "onDragging:" + isReachDeleteArea);
                        floatingWindowHelper.addView(floatWindowDeleteView, createDeleteLayoutParams());
                        if (tvDelete != null) {
                            tvDelete.setText(isReachDeleteArea ? "放开手删除" : "拖拽到此处删除");
                        }
                    }

                    @Override
                    public void onDragEnd(boolean isReachDeleteArea) {
                        Log.d("CBaymax", "onDragEnd:" + isReachDeleteArea);
                        floatingWindowHelper.removeView(floatWindowDeleteView);
                    }
                });
            }
        });
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicFloatLayout.dismiss();
            }
        });
    }

    /**
     * 创建删除布局的LayoutParams
     */
    private WindowManager.LayoutParams createDeleteLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = dp2px(this, 60);
        return layoutParams;
    }

    private int dp2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5F * (float) (dip >= 0.0F ? 1 : -1));
    }

}
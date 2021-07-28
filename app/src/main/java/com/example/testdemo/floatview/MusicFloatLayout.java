package com.example.testdemo.floatview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import static android.content.Context.WINDOW_SERVICE;

/**
 * @author oukanggui
 * @date 2021/7/6
 * @description 音乐悬浮窗布局
 */
public class MusicFloatLayout extends LinearLayout {
    /**
     * 窗口宽高
     */
    private int mWindowWidthPixels, mWindowHeightPixels;
    /**
     * 悬浮窗管理相关
     */
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private boolean mIsShow;
    private int mDeleteViewHeight = 0;
    /**
     * 记录按下时的x,y坐标
     */
    private int downX, downY;
    private int x;
    private int y;

    private OnDragListener onDragListener;

    private View.OnClickListener onClickListener;

    /**
     * 拖拽监听器
     */
    public interface OnDragListener {
        /**
         * 开始拖拽
         */
        void onDragStart();

        /**
         * 拖拽中
         *
         * @param isReachDeleteArea 是否到达删除区域
         */
        void onDragging(boolean isReachDeleteArea);

        /**
         * 拖拽结束
         *
         * @param isReachDeleteArea 是否到达删除区域
         */
        void onDragEnd(boolean isReachDeleteArea);
    }

    public MusicFloatLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MusicFloatLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MusicFloatLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MusicFloatLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 处理触摸事件，实现拖动、形状变更和粘边效果
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mWindowManager != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    downX = x;
                    downY = y;
                    if (onDragListener != null) {
                        onDragListener.onDragStart();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    mLayoutParams.x = mLayoutParams.x + movedX;
                    mLayoutParams.y = mLayoutParams.y + movedY;
                    if (mLayoutParams.x < 0) {
                        mLayoutParams.x = 0;
                    }
                    if (mLayoutParams.y < 0) {
                        mLayoutParams.y = 0;
                    }
                    mWindowManager.updateViewLayout(this, mLayoutParams);
                    if (onDragListener != null) {
                        onDragListener.onDragging(isDragViewReachDeleteArea());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    boolean isReachDeleteArea = isDragViewReachDeleteArea();
                    if (onDragListener != null) {
                        onDragListener.onDragEnd(isReachDeleteArea);
                    }
                    int upX = (int) event.getRawX();
                    int upY = (int) event.getRawY();
                    if (downX == upX && downY == upY) {
                        // 没有移动，当做点击处理
                        if (onClickListener != null) {
                            onClickListener.onClick(this);
                        }
                    } else {
                        if (!isReachDeleteArea) {
                            // 未到达删除区域
                            alignToWindowRight(getMeasuredWidth());
                        } else {
                            // 已达到删除区域
                            dismiss();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * @param context
     */
    private void init(Context context) {
        //悬浮窗管理相关
        mWindowManager = (WindowManager) getContext().getSystemService(WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.gravity = Gravity.START | Gravity.TOP;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        mLayoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        DisplayMetrics outMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(outMetrics);
        mWindowWidthPixels = outMetrics.widthPixels;
        mWindowHeightPixels = outMetrics.heightPixels;
        mDeleteViewHeight = dp2px(context, 60);
    }

    private int dp2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5F * (float) (dip >= 0.0F ? 1 : -1));
    }

    /**
     * 判断拖拽布局是否到达删除区域
     *
     * @return
     */
    private boolean isDragViewReachDeleteArea() {
        if ((mLayoutParams.y + getMeasuredHeight()) >= (mWindowHeightPixels - mDeleteViewHeight)) {
            return true;
        }
        return false;
    }


    /**
     * 控件更新到靠右的位置展示
     */
    public void alignToWindowRight(int width) {
        Log.d("CBaymax", "width = " + width);
        mLayoutParams.x = mWindowWidthPixels - width;
        invalidate();
        mWindowManager.updateViewLayout(this, mLayoutParams);
    }

    public void setOnCustomClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    /**
     * 显示悬浮窗
     * 一开始默认靠右居中位置
     */
    public void show(OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
        if (!mIsShow) {
            mLayoutParams.x = mWindowWidthPixels - getMeasuredWidth();
            mLayoutParams.y = mWindowHeightPixels / 2 - getMeasuredHeight() / 2;
            mWindowManager.addView(this, mLayoutParams);
            mIsShow = true;
        }
    }

    /**
     * dismiss
     */
    public void dismiss() {
        if (mIsShow) {
            mWindowManager.removeView(this);
            onDragListener = null;
            mIsShow = false;
        }
    }


}

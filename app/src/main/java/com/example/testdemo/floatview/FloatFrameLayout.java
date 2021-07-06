package com.example.testdemo.floatview;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.testdemo.R;

/**
 * 自定义浮窗
 * 内部添加两个ImageView，第一个显示浮窗图片，第二个显示关闭按钮图片
 * 如果只添加一个ImageView就不显示关闭按钮
 */
public class FloatFrameLayout extends FrameLayout {

    private float mStartX = 0;
    private float mStartY = 0;
    private float mTouchX = 0;
    private float mTouchY = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    private boolean mInitView = false;
    /**
     * 可拖拽的View
     */
    private View mDragView;
    /**
     * 底部固定的删除View
     */
    private View mDeleteView;
    private TextView mTvDelete;

    private int mDeleteViewHeight = 0;

    public FloatFrameLayout(Context context) {
        super(context);
    }

    public FloatFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mInitView) {
            mInitView = true;
            initView();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private int dp2px(Context context, float dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5F * (float) (dip >= 0.0F ? 1 : -1));
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //设置浮窗在最上层
        bringToFront();

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        if (mWidth == 0 || mHeight == 0) {
            mWidth = getResources().getDisplayMetrics().widthPixels;
            mHeight = getResources().getDisplayMetrics().heightPixels;
        }

        Log.d("CBaymax", "mParentWidth = " + mWidth);
        Log.d("CBaymax", "mParentHeight = " + mHeight);

        //浮窗图片设置触摸回调，用作处理移动浮窗位置
        mDragView = findViewById(R.id.view_drag);
        mDeleteView = findViewById(R.id.view_delete);
        mTvDelete = findViewById(R.id.tv_delete);
        if (mDeleteView != null) {
            mDeleteViewHeight = mDeleteView.getMeasuredHeight();
            if (mDeleteViewHeight <= 0) {
                mDeleteViewHeight = dp2px(getContext(), 60);
            }
        }
        if (mDragView != null) {
            mDragView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    float x = event.getRawX();
                    float y = event.getRawY();

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            mTouchX = x;
                            mTouchY = y;
                            mStartX = x;
                            mStartY = y;
                            break;
                        }

                        case MotionEvent.ACTION_MOVE: {
                            float moveX = x - mTouchX;
                            float moveY = y - mTouchY;
                            mTouchX = x;
                            mTouchY = y;
                            updateDrawViewPosition(moveX, moveY);
                            // 滑动过程中，显示删除布局
                            setDeleteViewVisibility(true);
                            if (mTvDelete != null) {
                                mTvDelete.setText(isDragViewReachDeleteArea() ? "放开手删除" : "拖拽到此处删除");
                            }
                            break;
                        }

                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_UP: {
                            setDeleteViewVisibility(false);
                            if (Math.abs(mTouchX - mStartX) < 10
                                    && Math.abs(mTouchY - mStartY) < 10) {
                                performClick();
                            } else {
                                if (isDragViewReachDeleteArea()) {
                                    // 删除
                                    mDragView.setVisibility(GONE);
                                } else {
                                    // 自动靠边
                                    mDragView.setVisibility(VISIBLE);
                                    forceDragViewAlignToParentRight();
                                }
                            }
                            break;
                        }

                        default:
                            break;
                    }
                    return true;
                }
            });
        }
        initDragViewPosition();
    }

    /**
     * 初始化浮窗位置，默认左侧中间对齐
     */
    private void initDragViewPosition() {
        if (mDragView == null) {
            return;
        }
        LayoutParams params = (LayoutParams) mDragView.getLayoutParams();
        if (params != null) {
            int dragViewWidth = mDragView.getMeasuredWidth();
            int dragViewHeight = mDragView.getMeasuredHeight();
            params.leftMargin = mWidth - dragViewWidth;
            params.topMargin = mHeight / 2 - dragViewHeight / 2;
            mDragView.setLayoutParams(params);
        }
    }

    /**
     * 更新拖拽View的坐标位置
     *
     * @param moveX 水平方向移动距离
     * @param moveY 垂直方向移动距离
     */
    private void updateDrawViewPosition(float moveX, float moveY) {
        if (mDragView == null) {
            return;
        }
        LayoutParams params = (LayoutParams) mDragView.getLayoutParams();
        // 更新浮动窗口位置参数，拖动过程中不受边距限制拖动范围
        if (params != null) {
            params.leftMargin += (int) (moveX);
            params.topMargin += (int) (moveY);
            int dragViewWidth = mDragView.getMeasuredWidth();
            int dragViewHeight = mDragView.getMeasuredHeight();
            // 越界检测
            if (params.leftMargin < 0) {
                params.leftMargin = 0;
            } else if (params.leftMargin + dragViewWidth > mWidth) {
                params.leftMargin = mWidth - dragViewWidth;
            }
            // 越界检测
            if (params.topMargin < 0) {
                params.topMargin = 0;
            } else if (params.topMargin + dragViewHeight > mHeight) {
                params.topMargin = mHeight - dragViewHeight;
            }
            mDragView.setLayoutParams(params);
        }
    }

    /**
     * 设置拖拽View在窗口中右对齐
     */
    private void forceDragViewAlignToParentRight() {
        if (mDragView == null) {
            return;
        }
        LayoutParams params = (LayoutParams) mDragView.getLayoutParams();
        if (params != null) {
            int dragViewWidth = mDragView.getMeasuredWidth();
            int dragViewHeight = mDragView.getMeasuredHeight();
            // 布局靠右
            params.leftMargin = mWidth - dragViewWidth;
            // 越界检测
            if (params.topMargin < 0) {
                params.topMargin = 0;
            }
            if (params.topMargin + dragViewHeight > mHeight) {
                params.topMargin = mHeight - dragViewHeight;
            }
            mDragView.setLayoutParams(params);
        }
    }

    /**
     * 判断拖拽布局是否到达删除区域
     *
     * @return
     */
    private boolean isDragViewReachDeleteArea() {
        if (mDragView == null || mDeleteView == null) {
            return false;
        }
        LayoutParams params = (LayoutParams) mDragView.getLayoutParams();
        if (params == null) {
            return false;
        }
        int dragViewHeight = mDragView.getMeasuredHeight();
        if ((params.topMargin + dragViewHeight) >= (mHeight - mDeleteViewHeight)) {
            return true;
        }
        return false;
    }

    /**
     * 设置删除布局可见性
     */
    private void setDeleteViewVisibility(boolean isShow) {
        if (mDeleteView != null) {
            mDeleteView.setVisibility(isShow ? VISIBLE : GONE);
        }
    }

}

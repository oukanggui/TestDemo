package com.example.testdemo.ring;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.testdemo.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author oukanggui
 * @date 2021/6/3
 * @description 自定义的带圆环进度的圆形头像ImageView
 */
public class ProgressCircleImageLayout extends FrameLayout {
    private CircleImageView circleImageView;

    /**
     * 圆环底部画笔
     */
    private Paint mProgressBottomPaint;
    /**
     * 进度圆环画笔
     */
    private Paint mProgressPaint;
    /**
     * 圆环的颜色
     */
    private int mRoundColor;
    /**
     * 圆环的宽度
     */
    private float mRoundWidth;
    /**
     * 圆环进度的颜色
     */
    private int mProgressColor;
    /**
     * 圆环进度的宽度
     */
    private float mProgressWidth;
    /**
     * 最大进度
     */
    private int max;
    /**
     * 进度条起始角度
     */
    private int mStartAngle;

    /**
     * 当前进度
     */
    private int mProgress;

    private static final int MSG_REFRESH_PROGRESS = 0x123;
    /**
     * 圆心的x坐标
     */
    private int mCenterX;
    /**
     * 圆环的半径
     */
    private int radius;
    /**
     * 是否正在进行动效刷新
     */
    private volatile boolean mIsRefreshing = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REFRESH_PROGRESS:
                    setProgress(msg.arg1);
                    break;
            }
        }

    };

    public ProgressCircleImageLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ProgressCircleImageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressCircleImageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressCircleImageLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化，进行控件和属性初始化工作
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        inflate(context, R.layout.layout_progress_circle_image, this);
        circleImageView = findViewById(R.id.iv_circle);
        if (attrs != null) {
            TypedArray typedArray = getResources().obtainAttributes(attrs, R.styleable.ProgressCircleImageLayout);
            // 获取自定义属性和默认值
            mRoundColor = typedArray.getColor(R.styleable.ProgressCircleImageLayout_pci_roundColor, Color.GRAY);
            mRoundWidth = typedArray.getDimension(R.styleable.ProgressCircleImageLayout_pci_roundWidth, 10);
            mProgressColor = typedArray.getColor(R.styleable.ProgressCircleImageLayout_pci_progressColor, Color.GREEN);
            mProgressWidth = typedArray.getDimension(R.styleable.ProgressCircleImageLayout_pci_progressWidth, mRoundWidth);
            max = typedArray.getInteger(R.styleable.ProgressCircleImageLayout_pci_max, 100);
            mStartAngle = typedArray.getInt(R.styleable.ProgressCircleImageLayout_pci_startAngle, 90);
            typedArray.recycle();
        }
        // 控制圆环不遮挡圆角图片
        circleImageView.setPadding((int) mProgressWidth, (int) mProgressWidth, (int) mProgressWidth, (int) mProgressWidth);
        // 画笔
        mProgressBottomPaint = new Paint();
        mProgressPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取圆心的x坐标
        mCenterX = getWidth() / 2;
        // 圆环的半径
        radius = (int) (mCenterX - mRoundWidth / 2);
        // step1 画最外层的大圆环
        drawOutArc(canvas);
        // step2 画圆弧-画圆环的进度
        drawProgressArc(canvas);
    }


    /**
     * 画最外层的大圆环
     */
    private void drawOutArc(Canvas canvas) {
        // 设置圆环的宽度
        mProgressBottomPaint.setStrokeWidth(mRoundWidth);
        // 设置圆环的颜色
        mProgressBottomPaint.setColor(mRoundColor);
        // 消除锯齿
        mProgressBottomPaint.setAntiAlias(true);
        // 设置画笔样式
        mProgressBottomPaint.setStyle(Paint.Style.STROKE);
        // 设置圆帽，保证圆弧两端是圆形
        mProgressBottomPaint.setStrokeCap(Paint.Cap.ROUND);
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(mCenterX - radius, mCenterX - radius, mCenterX + radius, mCenterX + radius);
        // 根据进度画圆弧
        canvas.drawArc(oval, mStartAngle, getRangeAngle(), false, mProgressBottomPaint);
    }

    /**
     * 画圆弧
     */
    private void drawProgressArc(Canvas canvas) {
        mProgressPaint.setColor(mProgressColor);
        // 消除锯齿
        mProgressPaint.setAntiAlias(true);
        // 设置画笔样式-空心
        mProgressPaint.setStyle(Paint.Style.STROKE);
        // 设置画笔的宽度使用进度条的宽度
        mProgressPaint.setStrokeWidth(mProgressWidth);
        // 设置圆帽，保证圆弧两端是圆形
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(mCenterX - radius, mCenterX - radius, mCenterX + radius, mCenterX + radius);
        // 根据进度画圆弧
        canvas.drawArc(oval, mStartAngle, getSweepAngle(), false, mProgressPaint);
    }

    /**
     * 获取进度圆弧扫描角度
     *
     * @return
     */
    private int getSweepAngle() {
//        float reviseMax;
//        if (mProgress < max){
//            // 需要修正角度，否则进度到达98%或99%时，进度弧的两端已经黏在了一起
//            float reviseAngle = 2 * radianToAngle();
//            reviseMax = max + max * reviseAngle / 360;
//        } else {
//            reviseMax = max;
//        }
        return getRangeAngle() * mProgress / max;
    }

    /**
     * 获取弧形总角度
     *
     * @return
     */
    private int getRangeAngle() {
        return 360 - 2 * (mStartAngle - 90);
    }

    /**
     * 已知圆半径和切线长，求弧长，并在弧长转换成角度返回
     *
     * @return
     */
    private float radianToAngle() {
        double sinA = mProgressWidth / 2 / radius;
        double aSin = Math.asin(sinA);
        return (float) Math.toDegrees(aSin);
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
        postInvalidate();
    }

    /**
     * 获取进度
     *
     * @return int 当前进度值
     */
    public synchronized int getProgress() {
        return mProgress;
    }

    /**
     * 设置进度，此为线程安全控件
     * 无动效
     *
     * @param progress 进度值
     */
    private synchronized void setProgress(int progress) {
        this.mProgress = correctProgress(progress);
        // 刷新界面调用postInvalidate()能在非UI线程刷新
        postInvalidate();
    }

    /**
     * 刷新进度条,此接口供外部调用
     * 有动效
     *
     * @param progress 进度值
     */
    public synchronized void refresh(int progress) {
        if (mIsRefreshing) {
            return;
        }
        final int correctProgress = correctProgress(progress);
        mHandler.removeCallbacksAndMessages(null);
        Thread thread = new Thread(new Runnable() {
            Message msg = null;

            @Override
            public void run() {
                int start = 0;
                while (start <= correctProgress) {
                    mIsRefreshing = true;
                    msg = new Message();
                    msg.what = MSG_REFRESH_PROGRESS;
                    msg.arg1 = start;
                    mHandler.sendMessage(msg);
                    start++;
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                    }
                }
                mIsRefreshing = false;
            }
        });
        thread.start();
    }

    /**
     * 对设置的进度值进行合法性纠正
     * @param progress
     * @return
     */
    private int correctProgress(int progress) {
        if (progress < 0) {
            //throw new IllegalArgumentException("mProgress not less than 0");
            progress = 0;
        }
        if (progress > max) {
            progress = max;
        }
        return progress;
    }
}

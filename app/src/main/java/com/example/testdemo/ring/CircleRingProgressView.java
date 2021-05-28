package com.example.testdemo.ring;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.testdemo.R;


/**
 * @author oukanggui
 * @date 2019/3/5
 * 描述：圆环形进度条
 */

public class CircleRingProgressView extends View {
    /**
     * 底部画笔
     */
    private Paint mPaint;
    /**
     * 进度圆环画笔
     */
    private Paint mProgressPaint;
    /**
     * 背景色画笔
     */
    private Paint mBackgroundPaint;
    /**
     * 背景色
     */
    private int mBgColor;
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
     * 内部渐变圆形的半径
     */
    private int innerRadius;
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

    public CircleRingProgressView(Context context) {
        this(context, null);
    }

    public CircleRingProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 画笔
        mPaint = new Paint();
        mProgressPaint = new Paint();
        mBackgroundPaint = new Paint();
        // 读取自定义属性的值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRingProgressView);
        // 获取自定义属性和默认值
        mRoundColor = mTypedArray.getColor(R.styleable.CircleRingProgressView_crp_roundColor, Color.RED);
        mRoundWidth = mTypedArray.getDimension(R.styleable.CircleRingProgressView_crp_roundWidth, 5);
        mProgressColor = mTypedArray.getColor(R.styleable.CircleRingProgressView_crp_progressColor, Color.GREEN);
        mProgressWidth = mTypedArray.getDimension(R.styleable.CircleRingProgressView_crp_progressWidth, mRoundWidth);
        max = mTypedArray.getInteger(R.styleable.CircleRingProgressView_crp_max, 100);
        mStartAngle = mTypedArray.getInt(R.styleable.CircleRingProgressView_crp_startAngle, 0);
        mBgColor = mTypedArray.getColor(R.styleable.CircleRingProgressView_crp_bgColor, Color.TRANSPARENT);
        mTypedArray.recycle();
//        //默认进度为100%
//        mProgress = max;
//        mRealProgress = mProgress;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取圆心的x坐标
        mCenterX = getWidth() / 2;
        // 圆环的半径
        radius = (int) (mCenterX - mRoundWidth / 2);
        innerRadius = (int) (mCenterX - mRoundWidth);
        // step1.1 画内部背景
        drawBackground(canvas);
        // step1 画最外层的大圆环
        drawOutArc(canvas);

        // step2 画圆弧-画圆环的进度
        drawProgressArc(canvas);

        // step3 画文字指示
        //drawCenterText(canvas);
    }

    /**
     * 画背景
     */
    private void drawBackground(Canvas canvas) {
        mBackgroundPaint.setColor(mBgColor);
        mBackgroundPaint.setStrokeWidth(0);
        // 消除锯齿
        mBackgroundPaint.setAntiAlias(true);
        // 设置画笔为填充样式
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(0, 0, getWidth(), getWidth());
        // 根据进度画圆弧
        canvas.drawArc(oval, 0, 360, false, mBackgroundPaint);
    }

    /**
     * 画最外层的大圆环
     */
    private void drawOutArc(Canvas canvas) {
        // 设置圆环的宽度
        mPaint.setStrokeWidth(mRoundWidth);
        // 设置圆环的颜色
        mPaint.setColor(mRoundColor);
        // 消除锯齿
        mPaint.setAntiAlias(true);
        // 设置画笔样式
        mPaint.setStyle(Paint.Style.STROKE);
        // 设置圆帽，保证圆弧两端是圆形
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        // 用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(mCenterX - radius, mCenterX - radius, mCenterX + radius, mCenterX + radius);
        // 根据进度画圆弧
        canvas.drawArc(oval, mStartAngle, getRangeAngle(), false, mPaint);
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

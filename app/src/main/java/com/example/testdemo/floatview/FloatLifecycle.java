package com.example.testdemo.floatview;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * 参考FloatWindow源码
 * 用于控制悬浮窗显示周期
 * 使用了三种方法针对返回桌面时隐藏悬浮按钮
 * 1.startCount计数，针对back到桌面可以及时隐藏
 * 2.监听home键，从而及时隐藏
 * 3.resumeCount计时，针对一些只执行onPause不执行onStop的奇葩情况
 */

public class FloatLifecycle extends BroadcastReceiver implements Application.ActivityLifecycleCallbacks {

    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final long delay = 300;
    private Handler mHandler;
    /**
     * 需要悬浮窗显示或隐藏的特定Activity
     */
    private Class[] activities;
    /**
     * 设置悬浮窗在activities中显示还是隐藏
     */
    private boolean showFlag;
    private int startCount;
    private int resumeCount;
    private boolean appBackground;
    private FloatLifecycleListener mFloatLifecycleListener;


    public FloatLifecycle(Context applicationContext, boolean showFlag, Class[] activities, FloatLifecycleListener floatLifecycleListener) {
        this.showFlag = showFlag;
        this.activities = activities;
        mFloatLifecycleListener = floatLifecycleListener;
        mHandler = new Handler(Looper.getMainLooper());
        ((Application) applicationContext).registerActivityLifecycleCallbacks(this);
        applicationContext.registerReceiver(this, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    /**
     * 判断在该Activity中，是否需要显示悬浮窗
     *
     * @param activity
     * @return
     */
    private boolean needShow(Activity activity) {
        if (activities == null) {
            return true;
        }
        for (Class a : activities) {
            if (a.isInstance(activity)) {
                return showFlag;
            }
        }
        return !showFlag;
    }


    @Override
    public void onActivityResumed(Activity activity) {
        resumeCount++;
        if (needShow(activity)) {
            mFloatLifecycleListener.onShow();
        } else {
            mFloatLifecycleListener.onHide();
        }
        appBackground = false;
    }

    @Override
    public void onActivityPaused(final Activity activity) {
        resumeCount--;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (resumeCount == 0) {
                    appBackground = true;
                    Log.d("CBaymax","onActivityPaused,onBackToDesktop");
                    mFloatLifecycleListener.onBackToDesktop();
                }
            }
        }, delay);

    }

    @Override
    public void onActivityStarted(Activity activity) {
        startCount++;
    }


    @Override
    public void onActivityStopped(Activity activity) {
        startCount--;
        if (startCount == 0) {
            Log.d("CBaymax","onActivityStopped,onBackToDesktop");
            mFloatLifecycleListener.onBackToDesktop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
            if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                Log.d("CBaymax","onReceive,onBackToDesktop");
                mFloatLifecycleListener.onBackToDesktop();
            }
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }
}

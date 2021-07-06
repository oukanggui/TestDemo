package com.example.testdemo.viewpager;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * @author oukanggui
 * @date 2021/6/21
 * @description
 */
public class ViewPagerTransformer implements ViewPager.PageTransformer {
    private int mOffset = 40;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void transformPage(@NonNull View page, float position) {
        Log.d("CBaymax", "position：" + position);
        if (position >= 0 && position <= 1) {
            page.setTranslationX(-position * page.getWidth());
            //缩放卡片并调整位置
            float scale = (page.getWidth() + mOffset * position) / page.getWidth();
            page.setScaleX(scale);
            page.setScaleY(scale);
            //移动Y轴坐标
            if (position == 0 || position == 1) {
                page.setTranslationY(-position * mOffset);
            }
            page.setTranslationZ(-position);
        }
    }
}

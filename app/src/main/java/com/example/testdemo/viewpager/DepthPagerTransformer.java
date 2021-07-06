package com.example.testdemo.viewpager;

import android.annotation.TargetApi;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author oukanggui
 * @date 2021/6/21
 * @description
 */
public class DepthPagerTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;
    private int mOffset = 40;

    @TargetApi(21)
    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // 隐藏左边的
            page.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            //使用默认的滑动效果
            page.setAlpha(1);
            page.setTranslationX(0);
            page.setScaleX(1);
            page.setScaleY(1);

            page.setTranslationZ(0);
            // 设置Y轴偏移
            page.setTranslationY(-position * mOffset);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.
            page.setAlpha(1);
            page.setTranslationZ(-1);
            // Counteract the default slide transition
            //设置偏移
            page.setTranslationX(pageWidth * -position);
            // 设置Y轴偏移
            page.setTranslationY(-position * mOffset);

            // Scale the page down (between MIN_SCALE and 1)
            //设置缩放因子
//            float scaleFactor = MIN_SCALE
//                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
//            page.setScaleX(scaleFactor);
//            page.setScaleY(scaleFactor);

        } else { // (1,+Infinity]
            // 隐藏右边的
            page.setAlpha(0);
        }
    }
}

package com.example.testdemo.stack;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author oukanggui
 * @date 2021/7/7
 * @description 层叠卡片布局管理器
 */
public class CardLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 自定义LayoutManager核心是摆放控件，所以onLayoutChildren方法是我们要改写的核心
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //缓存-在布局layout之前，将所有的子View先全部detach掉，然后放到Scrap集合里面缓存。
        detachAndScrapAttachedViews(recycler);

        //获取所有item(包括不可见的)个数
        int count = getItemCount();
        //由于我们是倒序摆放，所以初始索引从后面开始
        int initIndex = count - CardConfig.SHOW_MAX_COUNT;
        if (initIndex < 0) {
            initIndex = 0;
        }

        for (int i = initIndex; i < count; i++) {
            //从缓存中获取view
            View view = recycler.getViewForPosition(i);
            //添加到recyclerView
            addView(view);
            //测量一下view
            measureChild(view, 0, 0);

            //居中摆放，getDecoratedMeasuredWidth方法是获取带分割线的宽度，比直接使用view.getWidth()精确
            int realWidth = getDecoratedMeasuredWidth(view);
            int realHeight = getDecoratedMeasuredHeight(view);
            int widthPadding = (int) ((getWidth() - realWidth) / 2f);
            int heightPadding = (int) ((getHeight() - realHeight) / 2f);

            //摆放child
            layoutDecorated(view, widthPadding, heightPadding,
                    widthPadding + realWidth, heightPadding + realHeight);
            //根据索引，来位移和缩放child
            int level = count - i - 1;
            //level范围（CardConfig.SHOW_MAX_COUNT-1）- 0
            // 最下层的不动和最后第二层重叠
            if (level == CardConfig.SHOW_MAX_COUNT - 1) {
                level--;
            }
            // 设置Y轴位置偏移
            view.setTranslationY(level * CardConfig.TRANSLATION_Y);
            view.setScaleX(1 - level * CardConfig.SCALE);
            view.setScaleY(1 - level * CardConfig.SCALE);
        }
    }
}
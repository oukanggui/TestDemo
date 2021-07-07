package com.example.testdemo.stack;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.List;

/**
 * @author oukanggui
 * @date 2021/7/7
 * @description RecyclerView Item点击辅助类
 * ItemTouchHelper是一个强大的工具，它处理好了关于在RecyclerView上添加拖动排序与滑动删除的所有事情
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private List<String> mDatas;
    private RecyclerView.Adapter mAdapter;

    public ItemTouchHelperCallback(List<String> mDatas, RecyclerView.Adapter mAdapter) {
        this.mDatas = mDatas;
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        // 只允许左右滑动
        return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        // 实现循环效果
        String s = mDatas.remove(viewHolder.getLayoutPosition());
        mDatas.add(0, s);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //计算移动距离
        float distance = (float) Math.hypot(dX, dY);
        float maxDistance = recyclerView.getWidth() / 2f;

        //比例
        float fraction = distance / maxDistance;
        if (fraction > 1) {
            fraction = 1;
        }
        //为每个child执行动画
        int count = recyclerView.getChildCount();

        for (int i = 0; i < count; i++) {
            //获取的view从下层到上层
            View view = recyclerView.getChildAt(i);

            int level = CardConfig.SHOW_MAX_COUNT - i - 1;
            //level范围（CardConfig.SHOW_MAX_COUNT-1）-0，每个child最大只移动一个CardConfig.TRANSLATION_Y和放大CardConfig.SCALE

            if (level == CardConfig.SHOW_MAX_COUNT - 1) { // 最下层的不动和最后第二层重叠
                // 设置Y轴位置偏移
                view.setTranslationY(CardConfig.TRANSLATION_Y * (level - 1));
                view.setScaleX(1 - CardConfig.SCALE * (level - 1));
                view.setScaleY(1 - CardConfig.SCALE * (level - 1));
            } else if (level > 0) {
                // 设置Y轴位置偏移
                view.setTranslationY(level * CardConfig.TRANSLATION_Y - fraction * CardConfig.TRANSLATION_Y);
                view.setScaleX(1 - level * CardConfig.SCALE + fraction * CardConfig.SCALE);
                view.setScaleY(1 - level * CardConfig.SCALE + fraction * CardConfig.SCALE);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.3f;
    }
}

package com.example.testdemo.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testdemo.R;

import java.util.List;

/**
 * @author oukanggui
 * @date 2021/6/21
 * @description
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<String> dataList;

    public ViewPagerAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        position = position % dataList.size();
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(dataList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            if (dataList.size() == 1) {
                return 1;
            }
        }
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}

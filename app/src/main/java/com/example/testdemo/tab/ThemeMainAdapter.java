package com.example.testdemo.tab;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testdemo.R;

import java.util.List;

/**
 * @author oukanggui
 * @date 2021/5/25
 * @description
 */
public class ThemeMainAdapter extends RecyclerView.Adapter<ThemeMainAdapter.ViewHolder> {
    private Context mContext;
    private List<String> itemList;
    private int selectedIndex = 0;

    public ThemeMainAdapter(Context context) {
        this.mContext = context;
    }

    //接收数据
    public void setData(List<String> itemList) {
        this.itemList = itemList;
    }

    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.itemView.setId(position);

        holder.text_content.setText(itemList.get(position));

        if (selectedIndex == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#545454"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#DDDDDD"));
        }

        final int p = position;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectorListener.onSelect(v, p);
            }
        });

    }

    public interface OnSelectListener {
        void onSelect(View view, int position);
    }

    public void setOnSelectListener(OnSelectListener listener) {
        mSelectorListener = listener;
    }

    private OnSelectListener mSelectorListener;

    @Override
    public int getItemCount() {
        return itemList.size() == 0 ? 0 : itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_content;

        public ViewHolder(View itemView) {
            super(itemView);
            text_content = itemView.findViewById(R.id.tv_name);
        }
    }
}

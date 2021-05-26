package com.example.testdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author oukanggui
 * @date 2021/5/25
 * @description
 */
public class ThemeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_name);
        //得到数据
        String info = getArguments().getString("info");
        tv_title.setText(info);
        return view;
    }
}

package com.example.testdemo;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ThemeMainAdapter.OnSelectListener {

    private RecyclerView recyclerView;
    private CenterLayoutManager linearLayoutManager;
    private ThemeMainAdapter adapter;
    // 模拟数据
    private List<String> datasBeanList;
    private ThemeFragment themeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager = new CenterLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        //初始化recyclerView
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.addItemDecoration(new SpaceItemDecoration(getApplicationContext(), 5, 3));

        //初始化adapter
        adapter = new ThemeMainAdapter(this);
        adapter.setOnSelectListener(this);
    }

    private void initData() {
        //模拟数据
        datasBeanList = new ArrayList<>();
        datasBeanList.add("分类1");
        datasBeanList.add("分类2");
        datasBeanList.add("分类3");
        datasBeanList.add("分类4");
        datasBeanList.add("分类5");
        datasBeanList.add("分类6");
        datasBeanList.add("分类7");
        datasBeanList.add("分类8");
        datasBeanList.add("分类9");
        datasBeanList.add("分类10");
        datasBeanList.add("分类11");
        datasBeanList.add("分类12");
        datasBeanList.add("分类13");
        datasBeanList.add("分类14");
        datasBeanList.add("分类15");
        datasBeanList.add("分类16");
        datasBeanList.add("分类17");
        datasBeanList.add("分类18");
        datasBeanList.add("分类19");
        datasBeanList.add("分类20");
        datasBeanList.add("分类21");
        datasBeanList.add("分类22");
        datasBeanList.add("分类23");
        //数据处理
        dealWithData(datasBeanList);
    }

    private void dealWithData(List<String> datasBeanList) {
        //传递数据
        adapter.setData(datasBeanList);
        recyclerView.setAdapter(adapter);
        //创建Fragment对象
        themeFragment = new ThemeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, themeFragment);

        //传递数据到Fragment
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("info", datasBeanList.get(0));
        themeFragment.setArguments(mBundle);
        fragmentTransaction.commit();
    }

    @Override
    public void onSelect(View view, int position) {
        linearLayoutManager.smoothScrollToPosition(recyclerView, new RecyclerView.State(), position);
        //recyclerView.scrollToPosition();
        //选中处理
        adapter.setSelectedIndex(position);
        //刷新列表
        adapter.notifyDataSetChanged();

        //创建Fragment对象
        themeFragment = new ThemeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, themeFragment);

        //传递数据到Fragment
        Bundle mBundle = new Bundle();
        mBundle.putString("info", datasBeanList.get(position));
        themeFragment.setArguments(mBundle);
        fragmentTransaction.commit();
    }
}

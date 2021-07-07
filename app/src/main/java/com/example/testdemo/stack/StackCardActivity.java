package com.example.testdemo.stack;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testdemo.R;

import java.util.ArrayList;
import java.util.List;

public class StackCardActivity extends AppCompatActivity {

    RecyclerView rv;
    List<String> mStrings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_card);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new CardLayoutManager());

        for (int i = 1; i < 21; i++) {
            mStrings.add(String.valueOf(i));
        }
        MyAdapter myAdapter = new MyAdapter(this, mStrings);
        rv.setAdapter(myAdapter);

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(mStrings, myAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    class MyAdapter extends RecyclerView.Adapter {
        private Context mContext;
        private List<String> mStrings;

        public MyAdapter(Context mContext, List<String> mStrings) {
            this.mContext = mContext;
            this.mStrings = mStrings;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_stack_card, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.textView.setText(mStrings.get(i) + "/" + getItemCount());
        }

        @Override
        public int getItemCount() {
            return mStrings.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv);
            }
        }
    }
}
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
import android.widget.ImageView;

import com.example.testdemo.R;

import java.util.ArrayList;
import java.util.List;

public class StackCardActivity extends AppCompatActivity {

    RecyclerView rv;
    List<Integer> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_card);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new CardLayoutManager());

        imageList.add(R.mipmap.img1);
        imageList.add(R.mipmap.img2);
        imageList.add(R.mipmap.img3);
        imageList.add(R.mipmap.img4);
        imageList.add(R.mipmap.img5);
        MyAdapter myAdapter = new MyAdapter(this, imageList);
        rv.setAdapter(myAdapter);

        ItemTouchHelperCallback itemTouchHelperCallback = new ItemTouchHelperCallback(imageList, myAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }

    class MyAdapter extends RecyclerView.Adapter {
        private Context mContext;
        private List<Integer> imageList;

        public MyAdapter(Context mContext, List<Integer> imageList) {
            this.mContext = mContext;
            this.imageList = imageList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_stack_card, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
            myViewHolder.imageView.setImageResource(imageList.get(i));
        }

        @Override
        public int getItemCount() {
            return imageList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ic_pic);
            }
        }
    }
}
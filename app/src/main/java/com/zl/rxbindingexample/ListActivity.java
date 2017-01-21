package com.zl.rxbindingexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewChildAttachStateChangeEvent;
import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class ListActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private MyAdapter mAdapter;

    private static String TAG = "ListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        initViews();
        initData();
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        RxRecyclerView.childAttachStateChangeEvents(mRecyclerView).subscribe(new Action1<RecyclerViewChildAttachStateChangeEvent>() {
            @Override
            public void call(RecyclerViewChildAttachStateChangeEvent event) {

            }
        });

        RxRecyclerView.scrollEvents(mRecyclerView).subscribe(new Action1<RecyclerViewScrollEvent>() {
            @Override
            public void call(RecyclerViewScrollEvent event) {
                Log.i(TAG, "dx=" + event.dx() + ", dy=" + event.dy());
            }
        });

        RxRecyclerView.scrollStateChanges(mRecyclerView).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.i(TAG, integer + "");
            }
        });

        RxRecyclerViewAdapter.dataChanges(mAdapter).subscribe(new Action1<MyAdapter>() {
            @Override
            public void call(MyAdapter adapter) {

            }
        });

    }


    private void initData() {
        List<String> mList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            mList.add("Item" + i);
        }
        mAdapter.insertData(mList);
    }
}

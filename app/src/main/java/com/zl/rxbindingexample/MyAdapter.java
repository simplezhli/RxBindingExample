package com.zl.rxbindingexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context mContext;

    private List<String> mDatas = new ArrayList<>();

    public MyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public String getItem(int position) {
        return mDatas.get(Math.max(0, position));
    }

    public List<String> getDataSource() {
        return mDatas;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void clearAll() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void insertData(String newItem) {
        mDatas.add(newItem);
        notifyItemRangeInserted(getItemCount(), 1);
    }

    public void insertData(List<String> newItems) {
        int size = getItemCount();
        mDatas.addAll(newItems);
        if (size == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(size, newItems.size());
        }
    }

    public void delDate(int i) {
        mDatas.remove(i);
        notifyItemRemoved(i);
        if (i != mDatas.size()) {
            notifyItemRangeChanged(i, mDatas.size() - i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv)
        TextView mTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
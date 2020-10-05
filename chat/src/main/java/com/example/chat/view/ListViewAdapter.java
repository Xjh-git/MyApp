package com.example.chat.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class ListViewAdapter<T> extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int LayoutId;
    List<T> data;

    public ListViewAdapter(Context context, List<T> data, int layoutId) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        LayoutId = layoutId;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(context, convertView, parent, position, LayoutId);

        convert(holder, (T) data.get(position));

        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);
}

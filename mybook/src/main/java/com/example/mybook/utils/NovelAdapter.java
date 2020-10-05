package com.example.mybook.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mybook.R;
import com.example.mybook.bean.Novel;

import java.util.List;
import java.util.zip.Inflater;

public class NovelAdapter extends BaseAdapter {

    Context context;
    List<Novel> date;

    public NovelAdapter(Context context, List<Novel> date) {
        this.context = context;
        this.date = date;
    }

    @Override
    public int getCount() {
        return date.size();
    }

    @Override
    public Object getItem(int position) {
        return date.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder =null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.novel_item, parent, false);
            holder= new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.book_name_item);
            holder.writer = (TextView) convertView.findViewById(R.id.book_writer_item);
            holder.source = (TextView) convertView.findViewById(R.id.book_source_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Novel novel = date.get(position);
        holder.name.setText(novel.getName());
        holder.writer.setText("作者：" + novel.getWirter());
        holder.source.setText("来源：" + novel.getLink());
        return convertView;
    }

    class ViewHolder {
        TextView name, writer, source;
    }
}

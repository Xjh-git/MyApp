package com.example.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.chat.bean.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;

public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;

    public ChatMessageAdapter(Context context, List<ChatMessage> mDatas) {
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = mDatas.get(position);
        if (chatMessage.getType() == ChatMessage.Type.INCOMING) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = mDatas.get(position);
        ViewHolder holder = null;

        if (convertView == null) {
            //通过ItemType设置不同的布局
            if (getItemViewType(position) == 0) {
                convertView = mInflater.inflate(R.layout.item_incoming_msg, parent, false);
                holder = new ViewHolder();
                holder.mDate = (TextView) convertView.findViewById(R.id.id_incoming_date);
                holder.mMsg = (TextView) convertView.findViewById(R.id.id_incoming_msg_info);
                holder.mName = (TextView) convertView.findViewById(R.id.id_incoming_contacts_name);

            } else {
                convertView = mInflater.inflate(R.layout.item_outcoming_msg, parent, false);
                holder = new ViewHolder();
                holder.mDate = (TextView) convertView.findViewById(R.id.id_outcoming_date);
                holder.mMsg = (TextView) convertView.findViewById(R.id.id_outcoming_msg_info);
                holder.mName = (TextView)convertView.findViewById(R.id.id_outcoming_user_name);


            }
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        //设置数据
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        holder.mDate.setText(df.format(chatMessage.getDate()));
        holder.mMsg.setText(chatMessage.getMsg());
        holder.mName.setText(chatMessage.getSendUserName());

        return convertView;
    }

    private final class ViewHolder {
        TextView mDate;
        TextView mMsg,mName;
    }
}

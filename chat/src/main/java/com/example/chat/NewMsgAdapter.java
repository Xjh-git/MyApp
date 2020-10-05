package com.example.chat;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.bean.NewMessage;
import com.example.chat.view.ListViewAdapter;
import com.example.chat.view.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewMsgAdapter extends ListViewAdapter<NewMessage> {
    public NewMsgAdapter(Context context, List data) {
        super(context, data, R.layout.new_msg_item);
    }

    @Override
    public void convert(ViewHolder holder, NewMessage newMessage) {
        TextView name = (TextView) holder.getView(R.id.new_msg_item_name);
        ImageView icon = (ImageView) holder.getView(R.id.new_msg_item_icon);
        TextView tip = (TextView) holder.getView(R.id.new_msg_item_tip);
        TextView time = (TextView) holder.getView(R.id.new_msg_item_time);
        TextView msg = (TextView) holder.getView(R.id.new_msg_item_msg);

        //显示联系人姓名
        name.setText(newMessage.getContacts());
        //提示新的信息数量
        //        tip.setText(newMessage.getCount() + "");
        //        if (newMessage.getCount() > 0) {
        //            tip.setVisibility(View.VISIBLE);
        //        } else {
        //            tip.setVisibility(View.GONE);
        //        }
        //得到日期
        Date date = newMessage.getDate();

        //当天的消息直接显示几时几分，不是当天的消息显示日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat df;
        if (sdf.format(new Date()).equals(sdf.format(date))) {
            df = new SimpleDateFormat("HH:mm");
        } else {
            df = new SimpleDateFormat("MM-dd");
        }

        time.setText(df.format(date));
        msg.setText(newMessage.getMsg());

        if (newMessage.isRead()) {
            tip.setVisibility(View.GONE);
        } else {
            tip.setVisibility(View.VISIBLE);
        }
    }
}

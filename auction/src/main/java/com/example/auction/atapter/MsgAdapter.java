package com.example.auction.atapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.auction.R;
import com.example.auction.bean.Msg;

import java.text.SimpleDateFormat;
import java.util.List;

public class MsgAdapter extends ListViewAdapter<Msg> {
    public MsgAdapter(Context context, List<Msg> data) {
        super(context, data, R.layout.item_msg);
    }

    @Override
    public void convert(ViewHolder holder, Msg msg) {
        TextView msg_summary = holder.getView(R.id.msg_summary);
        TextView date = holder.getView(R.id.msg_time);

        SharedPreferences sp = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        int id = sp.getInt("id", -1);

        if (id == msg.getBuyer()) {
            msg_summary.setText(msg.getMsgBuyer());
        } else {
            msg_summary.setText(msg.getMsgSeller());
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        date.setText(df.format(msg.getMsgDate()));
    }
}

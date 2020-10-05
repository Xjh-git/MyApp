package com.example.chat.activitices.newFriend;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.utils.ConnServer;
import com.example.chat.utils.SocketUser;
import com.example.chat.view.ListViewAdapter;
import com.example.chat.view.ViewHolder;

import java.util.List;

/*
 * 好友申请列表的适配器
 * */
public class ContactsApplyAdapter extends ListViewAdapter<User> {

    TextView agree;

    AgreeApply agreeApply;

    public ContactsApplyAdapter(Context context, List data) {
        super(context, data, R.layout.contacts_apply_item);
    }


    @Override
    public void convert(final ViewHolder holder, final User user) {
        TextView apply_name = (TextView) holder.getView(R.id.contacts_apply_item_name);
        agree = (TextView) holder.getView(R.id.contacts_apply_item_agree);

        apply_name.setText(user.getUsername());
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreeApply.argee(user);
            }
        });
    }

    interface AgreeApply {
        void argee(User user);
    }

    public void setAgreeApply(AgreeApply agreeApply) {
        this.agreeApply = agreeApply;
    }
}

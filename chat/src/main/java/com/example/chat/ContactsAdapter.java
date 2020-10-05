package com.example.chat;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.bean.User;
import com.example.chat.view.ListViewAdapter;
import com.example.chat.view.ViewHolder;

import java.util.List;

public class ContactsAdapter extends ListViewAdapter<User> {
    public ContactsAdapter(Context context, List data) {
        super(context, data, R.layout.contacts_item);
    }

    @Override
    public void convert(ViewHolder holder, User user) {
        ImageView icon = (ImageView) holder.getView(R.id.contacts_item_icon);
        TextView name = (TextView) holder.getView(R.id.contacts_item_name);

        name.setText(user.getUsername());
    }
}

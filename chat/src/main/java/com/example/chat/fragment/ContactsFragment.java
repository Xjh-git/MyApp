package com.example.chat.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chat.ContactsAdapter;
import com.example.chat.R;
import com.example.chat.activitices.ContactsDetailsActivity;
import com.example.chat.activitices.newFriend.NewFriendsActivity;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.utils.ConnServer;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {

    LinearLayout newContacts;

    ListView contacts_list;
    ContactsAdapter adapter;
    List<User> contacts = new ArrayList<>();

    View view;

    User login_user;

    private static final String TAG = "tag1";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 123) {
                contacts.clear();
                List<String> contactsName = (List<String>) msg.obj;
                for (int i = 0; i < contactsName.size(); i++) {
                    User user = new User(contactsName.get(i), "abc");
                    contacts.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contacts_fragment, container, false);

        init();
        return view;
    }

    private void init() {
        //初始化控件
        initView();
        //初始化数据
        initDatas();
        //初始化事件
        ininListener();
    }

    private void ininListener() {

        //点击添加朋友
        newContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NewFriendsActivity.class);
                getActivity().startActivity(intent);
            }
        });

        //点击联系人，进入详情页
        contacts_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ContactsDetailsActivity.class);
                Bundle bundle = new Bundle();
                //获取username，通知详情页，此人是好友
                bundle.putString("username", contacts.get(position).getUsername());
                bundle.putBoolean("isContacts", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        contacts_list = (ListView) view.findViewById(R.id.contacts_list);
        newContacts = (LinearLayout) view.findViewById(R.id.id_user_new_contacts);
    }

    private void initDatas() {

        //得到登录信息
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String name = sp.getString("username", "abc");
        String psd = sp.getString("password", "abc");
        login_user = new User(name, psd);

        adapter = new ContactsAdapter(getActivity(), contacts);
        contacts_list.setAdapter(adapter);

        reflashData();

    }

    //刷新列表
    private void reflashData() {

        new Thread() {
            @Override
            public void run() {
                RequestMsg requestMsg = new RequestMsg(RequestMsg.GET_CONTACTS, login_user.getUsername());
                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                List<String> contactsName = resultMsg.getUserList();
                Message message = new Message();
                message.what = 123;
                message.obj = contactsName;
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        reflashData();
    }

}

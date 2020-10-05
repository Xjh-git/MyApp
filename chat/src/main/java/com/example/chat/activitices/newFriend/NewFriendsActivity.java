package com.example.chat.activitices.newFriend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.utils.ConnServer;
import com.example.chat.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/*
 * 显示好友申请列表的页面
 * */
public class NewFriendsActivity extends Activity implements ContactsApplyAdapter.AgreeApply {
    ImageView back_iv;
    TextView back_tv, add_friend_tv;

    ListView new_contacts_lv;
    List<User> new_contacts_list = new ArrayList<>();
    ContactsApplyAdapter applyAdapter;

    User user;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 123) {
                List<String> apply_list = (List<String>) msg.obj;
                for (int i = 0; i < apply_list.size(); i++) {
                    User apply_user = new User(apply_list.get(i), "abc");
                    new_contacts_list.add(apply_user);
                    applyAdapter.notifyDataSetChanged();
                }
            }
            if (msg.what == 111) {
                Bundle bundle = msg.getData();
                boolean result = bundle.getBoolean("res");
                if (result) {
                    User applyUser = (User) bundle.getSerializable("apply user");
                    new_contacts_list.remove(applyUser);
                    applyAdapter.notifyDataSetChanged();
                }
            }

        }
    };

    //    private final static String TAG = "tag1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_friends);

        init();

    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        back_iv = (ImageView) findViewById(R.id.back_to_contacts_iv);
        back_tv = (TextView) findViewById(R.id.back_to_contacts_tv);
        add_friend_tv = (TextView) findViewById(R.id.to_add_friend_tv);
        new_contacts_lv = (ListView) findViewById(R.id.contacts_apply_lv);

        Window window = getWindow();
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    private void initListener() {
        back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add_friend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewFriendsActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        //        User applyUser;
        //        for (int i = 0; i < 3; i++) {
        //            applyUser = new User("apply_user" + i + i, "123");
        //            new_contacts_list.add(applyUser);
        //        }

        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String name = sp.getString("username", "abc");
        String psd = sp.getString("password", "abc");
        this.user = new User(name, psd);


        applyAdapter = new ContactsApplyAdapter(this, new_contacts_list);
        new_contacts_lv.setAdapter(applyAdapter);

        //请求好友申请列表
        new Thread() {
            @Override
            public void run() {
                RequestMsg requestMsg = new RequestMsg(RequestMsg.GET_CONTACTS_APPLY, user.getUsername());
                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                List<String> apply_list = resultMsg.getUserList();
                Message message = new Message();
                message.what = 123;
                message.obj = apply_list;
                handler.sendMessage(message);
            }
        }.start();

        applyAdapter.setAgreeApply(this);
    }


    //同意好友申请
    @Override
    public void argee(final User applyUser) {
        new AlertDialog.Builder(this).
                setTitle("是否同意申请？").
                setNegativeButton("否", null).
                setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread() {
                            @Override
                            public void run() {
                                RequestMsg requestMsg = new RequestMsg(RequestMsg.CONTACTS_APPLY_AGREE, user.getUsername(), applyUser.getUsername());
                                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                                boolean result = false;
                                if (resultMsg != null) {
                                    result = resultMsg.getResult();
                                }
                                Message message = new Message();
                                message.what = 111;
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("apply user", applyUser);
                                bundle.putBoolean("res", result);
                                message.setData(bundle);
                                handler.sendMessage(message);

                            }
                        }.start();

                    }
                }).show();
    }
}

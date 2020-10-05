package com.example.chat.activitices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.activitices.newFriend.AddFriendActivity;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.utils.ConnServer;

public class ContactsDetailsActivity extends Activity {

    ImageView back_iv, icon;
    TextView back_tv, name, applyForContacts, send_message;

    String username;
    User login_user;

    boolean isClick = true;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123) {
                boolean result = (boolean) msg.obj;
                if (result) {
                    applyForContacts.setText("等待验证");
                    applyForContacts.setBackgroundColor(Color.parseColor("#ffffff"));
                    applyForContacts.setTextColor(Color.parseColor("#000000"));
                    isClick = false;
                } else {
                    Toast.makeText(ContactsDetailsActivity.this, "发送请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_contacts_details);
        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        back_iv = (ImageView) findViewById(R.id.contacts_details_back_iv);
        back_tv = (TextView) findViewById(R.id.contacts_details_back_tv);
        icon = (ImageView) findViewById(R.id.contacts_details_icon);
        name = (TextView) findViewById(R.id.contacts_details_name);
        applyForContacts = (TextView) findViewById(R.id.apply_for_contacts_tv);
        send_message = (TextView) findViewById(R.id.send_message);

    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        username = bundle.getString("username");
        boolean isContacts = bundle.getBoolean("isContacts");
        if (isContacts) {
            applyForContacts.setVisibility(View.GONE);
        } else {
            applyForContacts.setVisibility(View.VISIBLE);
        }
        name.setText(username);

        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String name = sp.getString("username", "abc");
        String psd = sp.getString("password", "abc");
        login_user = new User(name, psd);
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

        applyForContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isClick) {
                    //请求添加好友
                    new Thread() {

                        @Override
                        public void run() {
                            RequestMsg requestMsg = new RequestMsg(RequestMsg.CONTACTS_APPLY, login_user.getUsername(), username);
                            ResultMsg resultMsg = ConnServer.conn(requestMsg);
                            boolean result = false;
                            if (resultMsg != null) {
                                result = resultMsg.getResult();
                            }
                            Message message = new Message();
                            message.what = 123;
                            message.obj = result;
                            handler.sendMessage(message);
                        }
                    }.start();
                }
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsDetailsActivity.this, ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }
}

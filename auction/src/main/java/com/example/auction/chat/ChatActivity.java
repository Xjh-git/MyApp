package com.example.auction.chat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.chat.bean.ChatMessage;
import com.example.auction.chat.utils.HttpUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<ChatMessage> mDatas;

    private EditText mInputMsg;
    private Button mSendMsg;
    ImageView back;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //等待接收子线程数据返回
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            mDatas.add(fromMessage);
            mAdapter.notifyDataSetChanged();
            mMsgs.setSelection(mDatas.size());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        //初始化
        init();

    }

    private void init() {
        //申请权限
        initPermission();
        //初始化控件
        initView();
        //初始化数据
        initDatas();
        //初始化事件
        ininListener();
    }

    private void initPermission() {
        //申请联网权限
        if (ActivityCompat.checkSelfPermission(
                ChatActivity.this, Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChatActivity.this,
                    new String[]{Manifest.permission.INTERNET}, 123);
        }
    }

    private void ininListener() {
        mSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toMsg = mInputMsg.getText().toString();
                sendMsg(toMsg);
            }
        });

        mInputMsg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL) {
                    String toMsg = mInputMsg.getText().toString();
                    sendMsg(toMsg);
                }
                return true;
            }
        });

        mInputMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg=s.toString();
                if (msg.equals("")){
                    mSendMsg.setBackgroundColor(Color.parseColor("#cccccc"));
                }else {
                    mSendMsg.setBackgroundColor(Color.parseColor("#077ce1"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
        mInputMsg = (EditText) findViewById(R.id.id_input_msg_edt);
        mSendMsg = (Button) findViewById(R.id.id_send_msg_btn);
        back = (ImageView) findViewById(R.id.chat_back);
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        mDatas.add(new ChatMessage("你好，小熊为您服务", ChatMessage.Type.INCOMING, new Date()));
        mAdapter = new ChatMessageAdapter(this, mDatas);

        mMsgs.setAdapter(mAdapter);
    }

    private void sendMsg(final String toMsg) {
        if (TextUtils.isEmpty(toMsg)) {
            return;
        }
        ChatMessage toMessage = new ChatMessage();
        toMessage.setDate(new Date());
        toMessage.setMsg(toMsg);
        toMessage.setType(ChatMessage.Type.OUTCOMING);
        mDatas.add(toMessage);
        mAdapter.notifyDataSetChanged();
        mMsgs.setSelection(mDatas.size());

        mInputMsg.setText("");

        new Thread() {
            @Override
            public void run() {
                ChatMessage formMessage = HttpUtils.sendMessage(toMsg);
                Message message = Message.obtain();
                message.obj = formMessage;
                handler.sendMessage(message);
            }
        }.start();
    }
}

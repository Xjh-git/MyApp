package com.example.chat.activitices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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

import com.example.chat.ChatMessageAdapter;
import com.example.chat.R;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.NewMessage;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.database.ChatMessageDAO;
import com.example.chat.database.NewMsgDAO;
import com.example.chat.utils.ConnServer;
import com.example.chat.utils.SocketUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends Activity {
    EditText et_chat_message; //输入框
    ImageView contacts_back;  //返回箭头
    Button btn_chat_message_send;  //发送按钮

    TextView contacts_name_tv;  //显示联系人的username

    //聊天内容
    ListView lv_chat_dialog;
    List<ChatMessage> mDatas = new ArrayList<>();
    ChatMessageAdapter adapter;

    ChatMessageDAO chatMessageDAO;
    //用户信息
    User login_user;
    String contacts_name;  //联系人的username

    GetMsgThread mThread;
    MediaPlayer player;

    private final int GET_MSG = 111;
    private final int GET_NEW_MSG = 112;
    private final int SEND_MSG = 113;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SEND_MSG) {
                doByResult(msg.arg1);
            }
            if (msg.what == GET_NEW_MSG) {
                //获得新消息
                List<ChatMessage> chatMessageList = (List<ChatMessage>) msg.obj;
                if (chatMessageList.size() > 0) {
                    //有新消息，播放提示音
//                    player.start();
                    //保存消息列表（在首页显示用）
                    ChatMessage message = chatMessageList.get(chatMessageList.size() - 1);
                    NewMessage newMessage = new NewMessage(message.getMsg(), message.getDate(), message.getSendUserName(), 0, true);
                    saveMsgList(login_user.getUsername(), newMessage);
                    //将消息显示在界面上
                    for (int i = 0; i < chatMessageList.size(); i++) {
                        ChatMessage chatMessage = chatMessageList.get(i);
                        if (chatMessage.getSendUserName().equals(login_user.getUsername())) {
                            chatMessage.setType(ChatMessage.Type.OUTCOMING);
                        } else {
                            chatMessage.setType(ChatMessage.Type.INCOMING);
                        }
                        //将消息保存在本地数据库
//                        chatMessage.setDate(new Date());
                        chatMessageDAO.insert(chatMessage);
                        mDatas.add(chatMessage);
                    }
                    adapter.notifyDataSetChanged();
                    lv_chat_dialog.setSelection(mDatas.size());
                }
            }
            if (msg.what == GET_MSG) {
                List<ChatMessage> data = (List<ChatMessage>) msg.obj;
                if (data != null) {
                    for (int i = 0; i < data.size(); i++) {
                        mDatas.add(data.get(i));
                        adapter.notifyDataSetChanged();
                        lv_chat_dialog.setSelection(mDatas.size());
                    }
                }
            }
        }
    };

    private final static String TAG = "tag1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chat);

        init();
        mThread = new GetMsgThread();
        mThread.start();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initData() {
        //获得联系人的username
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        contacts_name = bundle.getString("username");

        //得到登录用户的信息
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String username = sp.getString("username", "abc");
        String password = sp.getString("password", "abc");
        login_user = new User(username, password);


        //初始化控件的数据
        contacts_name_tv.setText(contacts_name);
        adapter = new ChatMessageAdapter(this, mDatas);
        lv_chat_dialog.setAdapter(adapter);
        lv_chat_dialog.setSelection(mDatas.size());

        getMsg();
        //让listview自动滚到最底部
        lv_chat_dialog.setSelection(mDatas.size());

        player = MediaPlayer.create(this, R.raw.msg);

        chatMessageDAO = new ChatMessageDAO(this);
    }

    private void initView() {
        et_chat_message = (EditText) findViewById(R.id.et_chat_message);
        contacts_back = (ImageView) findViewById(R.id.contacts_back);
        btn_chat_message_send = (Button) findViewById(R.id.btn_chat_message_send);
        lv_chat_dialog = (ListView) findViewById(R.id.lv_chat_dialog);
        contacts_name_tv = (TextView) findViewById(R.id.chat_contacts_name_tv);
    }

    private void initListener() {
        //监听返回点击事件
        contacts_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //监听发送按钮的点击事件
        btn_chat_message_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送消息，清空输入框
                sendMsg();

                //保存消息列表（在首页显示用）
                NewMessage newMessage = new NewMessage(et_chat_message.getText().toString(), new Date(),
                        contacts_name, 0, true);
                saveMsgList(login_user.getUsername(), newMessage);

                et_chat_message.setText("");
            }
        });

        //输入时点回车直接发送消息
        //暂时不用回车键直接发送消息
        /*
        et_chat_message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL) {
                    //发送消息，清空输入框
                    sendMsg();


                    //保存消息列表（在首页显示用）
                    NewMessage newMessage = new NewMessage(et_chat_message.getText().toString(), new Date(),
                            contacts_name, 0, true);
                    saveMsgList(login_user.getUsername(), newMessage);

                    et_chat_message.setText("");
                }
                return true;
            }
        });*/
    }

    ChatMessage chatMessage;

    private void sendMsg() {
        //发送内容不能为空
        String msg = et_chat_message.getText().toString();
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        chatMessage = new ChatMessage(msg, new Date(), login_user.getUsername(), contacts_name, ChatMessage.Type.OUTCOMING);

        //子线程进行网络操作
        new Thread() {
            @Override
            public void run() {

                //发送消息
                RequestMsg requestMsg = new RequestMsg(RequestMsg.SEND_MSG, chatMessage);
                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                int sendID = resultMsg.getSendId();

                //将发送的结果取出来
                Message message = new Message();
                message.what = SEND_MSG;
                message.arg1 = sendID;
                handler.sendMessage(message);
            }
        }.start();
    }


    //根据发送结果操作
    private void doByResult(int result) {
        if (result > 0) {
            //发送成功，插入本地数据库
            mDatas.add(chatMessage);
            adapter.notifyDataSetChanged();
            lv_chat_dialog.setSelection(mDatas.size());

        } else {
            Toast.makeText(this, "发送失败！", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMsg() {
        new Thread() {
            @Override
            public void run() {
                //得到新消息后，删除新消息表的数据
                RequestMsg requestMsg = new RequestMsg(RequestMsg.DELETE_NEW_MSG, login_user.getUsername(), contacts_name);
                ConnServer.conn(requestMsg);

                //请求获得两人的所有聊天记录
                requestMsg = new RequestMsg(RequestMsg.GET_READ_MSG, login_user.getUsername(), contacts_name);
                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                if (resultMsg.getChatMessageList() != null) {
                    List<ChatMessage> data = resultMsg.getChatMessageList();
                    Message message = new Message();
                    message.what = GET_MSG;
                    message.obj = data;
                    handler.sendMessage(message);
                }
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void saveMsgList(String user, NewMessage msg) {
        NewMsgDAO newMsgDAO = new NewMsgDAO(this);
        newMsgDAO.update(user, msg);
    }

    //控制每一秒刷新一次数据
    class GetMsgThread extends Thread {
        boolean stop = false;

        public void stopByMark() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                getNewMsg();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mThread.stopByMark();
    }

    private void getNewMsg() {
        RequestMsg requestMsg = new RequestMsg(RequestMsg.GET_CONTACTS_NEW_MSG, login_user.getUsername(), contacts_name);
        ResultMsg resultMsg = ConnServer.conn(requestMsg);
        List<ChatMessage> chatMessageList = resultMsg.getChatMessageList();
        Message message = new Message();
        message.what = GET_NEW_MSG;
        message.obj = chatMessageList;
        handler.sendMessage(message);
    }
}

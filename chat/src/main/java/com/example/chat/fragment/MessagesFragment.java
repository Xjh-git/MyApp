package com.example.chat.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.chat.NewMsgAdapter;
import com.example.chat.R;
import com.example.chat.Test.TestClient;
import com.example.chat.activitices.ChatActivity;
import com.example.chat.bean.ChatMessage;
import com.example.chat.bean.NewMessage;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.database.NewMsgDAO;
import com.example.chat.utils.ConnServer;
import com.example.chat.utils.SocketUser;
import com.example.chat.view.UpListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MessagesFragment extends Fragment implements UpListView.ILoadLister {

    UpListView chat_lv;
    NewMsgAdapter adapter;
    List<NewMessage> list = new ArrayList<>();

    User login_user;

    View view;
    MediaPlayer player;

    ReflashThread mThread;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123) {
                //得到新消息时更新数据
                List<NewMessage> newMessageList = (List<NewMessage>) msg.obj;
                //循环判断每一个数据，已经在列表中的就更新一下，不再列表中的就插入
                for (int i = 0; i < newMessageList.size(); i++) {
                    NewMessage newMessage = newMessageList.get(i);
                    NewMessage listMsg = null;
                    boolean isCon = false;
                    for (int j = 0; j < list.size(); j++) {
                        listMsg = list.get(j);
                        if (newMessage.getContacts().equals(listMsg.getContacts())) {
                            isCon = true;
                            break;
                        }
                    }
                    if (isCon) {
                        list.remove(listMsg);
                        list.add(0, newMessage);
                    } else {
                        list.add(0, newMessage);
                    }
                    //将更新的数据保存在本地数据库
                    saveMsgList(login_user.getUsername(), newMessageList.get(newMessageList.size() - 1));
                    Log.d(TAG, "handleMessage: " + newMessageList.get(newMessageList.size() - 1).getMsg());
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    private static String TAG = "tag1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_fragment, container, false);


        init();
        Log.d(TAG, "onCreateView: ");
        return view;

    }

    private void init() {
        //初始化view
        initView();
        //初始化数据
        initData();
        //初始化listener
        initListener();
    }

    private void initListener() {

        chat_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //改变显示状态
                NewMessage message = list.get(position - 1);
                message.setRead(true);
                adapter.notifyDataSetChanged();

                //将信息保存在本地数据库，方便下次读取
                saveMsgList(login_user.getUsername(), message);

                //启动聊天页面，将联系人的id发送过去
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", message.getContacts());
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });


    }


    private void initData() {
        //消息提示音
        player = MediaPlayer.create(getActivity(), R.raw.msg);

        //得到登录信息
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String username = sp.getString("username", "abc");
        String password = sp.getString("password", "abc");
        login_user = new User(username, password);


        adapter = new NewMsgAdapter(getActivity(), list);
        chat_lv.setAdapter(adapter);

        //下拉刷新的回调接口
        chat_lv.setInterface(this);

        registerForContextMenu(chat_lv);
    }

    private void initView() {
        chat_lv = (UpListView) view.findViewById(R.id.chat_new_msg_lv);
    }

    //初始化长按弹出的上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.manage, menu);
        MenuItem item = menu.findItem(R.id.isRead);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        NewMessage msg = list.get(info.position - 1);
        if (msg.isRead()) {
            item.setTitle("标记未读");
        } else {
            item.setTitle("标记已读");
        }
    }

    //点击弹出菜单的item
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.isRead:
                NewMessage msg = list.get(info.position - 1);
                if (msg.isRead()) {
                    msg.setRead(false);
                    list.remove(msg);
                    list.add(0, msg);
                } else {
                    msg.setRead(true);
                }
                saveMsgList(login_user.getUsername(), msg);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.up:
                Toast.makeText(getActivity(), "敬请期待...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete:
                Toast.makeText(getActivity(), "敬请期待...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }


    //下拉刷新时 刷新数据
    @Override
    public void onReflash() {

        //手动加上0.5s的延迟
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                reflashData();
                chat_lv.reflashComplete();
            }
        }, 500);
    }

    //刷新数据
    private void reflashData() {

        new Thread() {
            @Override
            public void run() {
                //发送的请求
                RequestMsg requestMsg = new RequestMsg(RequestMsg.GET_NEW_MSG, login_user);
                //得到响应
                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                //得到返回的消息列表
                List<NewMessage> newMessageList = resultMsg.getNewMessageList();
                //列表不空，发出提示音，更新数据
                if (newMessageList.size() > 0) {
//                    player.start();
                    Message message = new Message();
                    message.what = 123;
                    message.obj = newMessageList;
                    handler.sendMessage(message);
                }
            }
        }.start();

    }

    private void saveMsgList(String user, NewMessage msg) {
        NewMsgDAO newMsgDAO = new NewMsgDAO(getActivity());
        newMsgDAO.update(user, msg);
    }

    private void selectSaveList() {
        list.clear();
        NewMsgDAO newMsgDAO = new NewMsgDAO(getActivity());
        List<NewMessage> selectList = newMsgDAO.select(login_user.getUsername());
        list.addAll(selectList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        selectSaveList();

        Log.d(TAG, "onResume: ");
        //子线程监听是否来消息
        mThread = new ReflashThread();
        mThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            if (player.isPlaying())
                player.stop();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mThread.isAlive())
            mThread.stopByMark();
    }

    //控制每一秒刷新一次数据
    class ReflashThread extends Thread {
        boolean stop = false;

        public void stopByMark() {
            stop = true;
        }

        @Override
        public void run() {
            while (!stop) {
                reflashData();
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

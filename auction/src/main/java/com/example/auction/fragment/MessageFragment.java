package com.example.auction.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.atapter.MsgAdapter;
import com.example.auction.bean.Msg;
import com.example.auction.chat.ChatActivity;
import com.example.auction.database.MsgDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageFragment extends Fragment {
    private ListView mMsgs;
    private MsgAdapter mAdapter;
    private List<Msg> mDatas;

    TextView smart;

    int loginID;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_fragment, container, false);

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
        smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                getActivity().startActivity(intent);
            }
        });

        mMsgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String str;
                Msg msg = mDatas.get(position);
                if (loginID == msg.getBuyer()) {
                    str = msg.getMsgBuyer();
                } else {
                    str = msg.getMsgSeller();
                }
                new AlertDialog.Builder(getActivity()).
                        setTitle("详情").
                        setMessage(str).setPositiveButton("关闭", null).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Msg msg = mDatas.get(position);
                        MsgDAO msgDAO = new MsgDAO(getActivity());
                        msgDAO.deleteMsg(msg);
                        mDatas.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });

    }

    private void initView() {
        mMsgs = (ListView) view.findViewById(R.id.auction_msg);
        smart = (TextView) view.findViewById(R.id.smart_server);
        registerForContextMenu(mMsgs);

    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        loginID = sp.getInt("id", -1);

        //        Msg msg;
        //        for (int i = 0; i < 5; i++) {
        //            msg = new Msg(loginID, 11, "您已成功购买一件商品，点击查看详情", "您寄卖的商品已成功售出", new Date(), false, false);
        //            mDatas.add(msg);
        //        }
        MsgDAO msgDAO = new MsgDAO(getActivity());
        mDatas = msgDAO.selectMsg(loginID);
        mAdapter = new MsgAdapter(getActivity(), mDatas);
        mMsgs.setAdapter(mAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.msg_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                Msg msg = mDatas.get(info.position - 1);
                MsgDAO msgDAO = new MsgDAO(getActivity());
                msgDAO.deleteMsg(msg);
                mDatas.remove(info.position - 1);
                mAdapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }
}

package com.example.chat.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.activitices.SettingActivity;

import static android.app.Activity.RESULT_OK;

/*
 * 个人主页
 * */
public class UserHomePageFragment extends Fragment {

    TextView userName, userID;
    ImageView userIcon, setting;

    View view;
    LinearLayout info;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage_fragment, container, false);

        init();
        return view;
    }

    private void init() {
        initView();
        initData();
        initListener();

    }

    private void initData() {
        //得到登陆客户的信息
        SharedPreferences sp = getActivity().getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        String username = sp.getString("username", "abc");

        userName.setText(username);
        userID.setText("账号：" + username);

    }

    private void initListener() {


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivityForResult(intent, 123);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "暂未开放该功能", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initView() {
        userID = (TextView) view.findViewById(R.id.id_user_id);
        userName = (TextView) view.findViewById(R.id.id_user_name);
        userIcon = (ImageView) view.findViewById(R.id.user_icon);
        setting = (ImageView) view.findViewById(R.id.to_setting);
        info = (LinearLayout) view.findViewById(R.id.id_user_information);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            getActivity().finish();
        }
    }
}

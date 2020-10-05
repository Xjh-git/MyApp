package com.example.auction.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.activity.information.SettingActivity;
import com.example.auction.activity.information.userInformation.UserPayActivity;
import com.example.auction.bean.UserInfo;
import com.example.auction.database.UserDAO;

import static android.app.Activity.RESULT_OK;

public class UserHomePageFragment extends Fragment {

    TextView userName, userID, userMoney;
    LinearLayout toUserPay, toUserOrder;
    ImageView userIcon, setting;
    UserInfo user;
    View view;
    LinearLayout address, info;

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
        int id = sp.getInt("id", -1);
        if (id == -1) {
            return;
        }
        UserDAO userDAO = new UserDAO(getActivity());
        user = userDAO.select(id);

        userIcon.setImageURI(Uri.parse(user.getIcon()));
        userName.setText(user.getName());
        userID.setText("id:" + String.valueOf(user.getId()));
        userMoney.setText(String.valueOf(user.getMoney()));
    }

    private void initListener() {
        toUserPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserPayActivity.class);
                startActivity(intent);
            }
        });

        //点击前往我的主页
        toUserOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Intent intent = new Intent(UserHomepageActivity.this, UserOrderActivity.class);
                //                startActivity(intent);
                Toast.makeText(getActivity(), "暂未开放该功能", Toast.LENGTH_SHORT).show();
            }
        });

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

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "暂未开放该功能", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        userID = (TextView) view.findViewById(R.id.id_user_id);
        userName = (TextView) view.findViewById(R.id.id_user_name);
        toUserOrder = (LinearLayout) view.findViewById(R.id.id_user_order);
        toUserPay = (LinearLayout) view.findViewById(R.id.id_user_money);
        userIcon = (ImageView) view.findViewById(R.id.user_icon);
        userMoney = (TextView) view.findViewById(R.id.user_money);
        setting = (ImageView) view.findViewById(R.id.to_setting);
        address = (LinearLayout) view.findViewById(R.id.id_user_address);
        info = (LinearLayout) view.findViewById(R.id.id_user_information);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            getActivity().finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}

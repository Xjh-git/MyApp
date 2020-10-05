package com.example.auction.activity.information.userInformation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.bean.UserInfo;
import com.example.auction.database.UserDAO;

public class UserPayActivity extends Activity {
    TextView userMoney, money_datails;
    ImageView back;
    LinearLayout recharge, withdrawal;
    UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_pay);
        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        userMoney = (TextView) findViewById(R.id.user_money);
        recharge = (LinearLayout) findViewById(R.id.id_user_recharge);
        withdrawal = (LinearLayout) findViewById(R.id.id_user_withdrawal);
        back = (ImageView) findViewById(R.id.mon_back);
        money_datails = (TextView) findViewById(R.id.user_money_details);
    }

    private void initData() {
        //得到登陆客户的信息
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        int id = sp.getInt("id", -1);
        if (id == -1) {

            return;
        }
        UserDAO userDAO = new UserDAO(this);
        user = userDAO.select(id);

        userMoney.setText(String.valueOf(user.getMoney()));
    }

    private void initListener() {
        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPayActivity.this, UserRechargeActivity.class);
                startActivityForResult(intent, 123);
            }
        });
        withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                Toast.makeText(UserPayActivity.this, "暂未开放该功能", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserPayActivity.this, WithdrawalActivity.class);
                startActivityForResult(intent, 111);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        money_datails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserPayActivity.this, "暂未开放该功能", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            initData();
        }
        if (requestCode == 111 && resultCode == RESULT_OK) {
            initData();
        }
    }
}

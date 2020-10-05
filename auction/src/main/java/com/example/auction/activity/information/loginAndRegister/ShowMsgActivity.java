package com.example.auction.activity.information.loginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.auction.R;

public class ShowMsgActivity extends Activity {
    TextView userInfo_tv, OK_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_msg);

        init();
    }

    private void init() {

        initView();
        initData();
        iniListener();
    }

    private void initView() {
        userInfo_tv = (TextView) findViewById(R.id.user_info);
        OK_tv = (TextView) findViewById(R.id.id_msgOK);
    }

    private void iniListener() {
        OK_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        userInfo_tv.setText("您的id为：" + id);
    }
}

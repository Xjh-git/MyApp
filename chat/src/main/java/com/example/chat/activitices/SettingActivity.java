package com.example.chat.activitices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.loginAndRegister.LoginActivity;

/*
 * 设置页面
 * */
public class SettingActivity extends Activity {

    TextView log_off_tv;
    ImageView back;
    boolean isLogin;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {
        initView();
        initListener();
        initData();
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.setting_back);
        log_off_tv = (TextView) findViewById(R.id.to_log_off);
    }

    private void initData() {
        sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        isLogin = sp.getBoolean("isLogin", false);

        if (isLogin) {
            log_off_tv.setText("注销登录");
        } else {
            log_off_tv.setText("登录");
        }
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击注销登录，改变登录状态、finish自身，同时通知mainactivity finish
        log_off_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("isLogin", false);
                    editor.commit();
                }
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                setResult(RESULT_OK);
                finish();
            }
        });

    }
}

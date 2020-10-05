package com.example.chat.loginAndRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.MainActivity;
import com.example.chat.R;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.utils.ConnServer;
import com.example.chat.utils.SocketUser;

/*
 * 登录页面
 * */
public class LoginActivity extends Activity {
    EditText getID_edt, get_password_edt;
    TextView id_user_login, loss_password, to_register, tip;
    String username, password;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123) {
                doByResult((boolean) msg.obj);
            }
        }
    };

    private static final int LoginSuccess = 1;
    private static final int LoginFail = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        initView();
        initListener();
    }

    private void initView() {
        getID_edt = (EditText) findViewById(R.id.getID_edt);
        get_password_edt = (EditText) findViewById(R.id.get_password_edt);
        id_user_login = (TextView) findViewById(R.id.id_user_login);
        loss_password = (TextView) findViewById(R.id.loss_password);
        to_register = (TextView) findViewById(R.id.to_register);
        tip = (TextView) findViewById(R.id.login_tip);
        tip.setVisibility(View.GONE);
    }

    private void initListener() {

        //点击登录、进行登录的处理
        id_user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //点击注册、前往注册页面
        to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //点击忘记密码，后续操作还未完成
        loss_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "哈哈哈，活该，哈哈哈...", Toast.LENGTH_SHORT).show();
            }
        });

        get_password_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    username = getID_edt.getText().toString();
                    password = get_password_edt.getText().toString();
                    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                        tip.setText("用户名或密码不能为空");
                        tip.setVisibility(View.VISIBLE);
                    } else {
                        tip.setVisibility(View.GONE);
                    }
                }else {
                    if (tip.getVisibility() == View.VISIBLE) {
                        tip.setVisibility(View.GONE);
                    }
                }
            }
        });
        getID_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (tip.getVisibility() == View.VISIBLE) {
                        tip.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    ;
    User user;

    private void login() {
        username = getID_edt.getText().toString();
        password = get_password_edt.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            tip.setText("用户名或密码不能为空");
            tip.setVisibility(View.VISIBLE);
            return;
        }
        tip.setVisibility(View.GONE);

        user = new User(username, password);

        new Thread() {
            @Override
            public void run() {
                RequestMsg requestMsg = new RequestMsg(RequestMsg.LOGIN, user);
                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                boolean login_result = false;
                if (resultMsg != null) {
                    login_result = resultMsg.getResult();
                }
                Message message = new Message();
                message.what = 123;
                message.obj = login_result;
                handler.sendMessage(message);
            }
        }.start();

    }

    private void doByResult(boolean login_result) {
        if (login_result) {
            //登录成功,保存登录信息
            saveUserInfo();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            //登录失败
            setLoginInfo();
            tip.setVisibility(View.VISIBLE);
            tip.setText("用户名或密码不正确");
        }
    }

    //保存登录信息
    private void saveUserInfo() {
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.commit();
    }

    private void setLoginInfo() {
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false);
        editor.commit();
    }
}

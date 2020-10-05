package com.example.chat.loginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.R;
import com.example.chat.bean.RequestMsg;
import com.example.chat.bean.ResultMsg;
import com.example.chat.bean.User;
import com.example.chat.utils.ConnServer;
import com.example.chat.utils.SocketUser;

public class RegisterActivity extends Activity {

    EditText set_id_edt, set_password_edt, set_repassword_edt;
    TextView id_user_register, register_result, tip;
    LinearLayout cancel;

    private static final String TAG = "tag1";

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123) {
                int time = msg.arg1;
                register_result.setText("注册成功，即将前往登录界面，" + time + "...");
                if (time == 0)
                    finish();
            }
            if (msg.what == 111) {
                doByResult((boolean) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();

    }


    private void init() {
        initView();
        initListener();
    }

    private void initView() {
        set_id_edt = (EditText) findViewById(R.id.set_id_edt);
        set_password_edt = (EditText) findViewById(R.id.set_password_edt);
        set_repassword_edt = (EditText) findViewById(R.id.set_repassword_edt);
        id_user_register = (TextView) findViewById(R.id.id_user_register);
        register_result = (TextView) findViewById(R.id.register_result);
        register_result.setVisibility(View.GONE);
        cancel = (LinearLayout) findViewById(R.id.cancel);
        tip = (TextView) findViewById(R.id.register_tip);
        tip.setVisibility(View.GONE);
    }

    private void initListener() {
        id_user_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        set_id_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (tip.getVisibility() == View.VISIBLE) {
                        tip.setVisibility(View.GONE);
                    }
                }
            }
        });

        set_password_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (tip.getVisibility() == View.VISIBLE) {
                        tip.setVisibility(View.GONE);
                    }
                }
            }
        });

        set_repassword_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

    User user;

    private void register() {
        String username, password, repassword;
        username = set_id_edt.getText().toString();
        password = set_password_edt.getText().toString();
        repassword = set_repassword_edt.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            tip.setText("用户名或密码不能为空");
            tip.setVisibility(View.VISIBLE);
            return;
        }

        if (!password.equals(repassword)) {
            tip.setText("两次输入的密码不一致!");
            tip.setVisibility(View.VISIBLE);
            return;
        }
        tip.setVisibility(View.GONE);

        user = new User(username, password);
        new Thread() {
            @Override
            public void run() {
                RequestMsg requestMsg = new RequestMsg(RequestMsg.REGISTER, user);
                ResultMsg resultMsg = ConnServer.conn(requestMsg);
                boolean registerResult = false;
                if (resultMsg != null) {
                    registerResult = resultMsg.getResult();
                }

                Message message = new Message();
                message.what = 111;
                message.obj = registerResult;
                handler.sendMessage(message);
            }
        }.start();


    }


    private void doByResult(boolean registerResult) {
        if (!registerResult) {
            tip.setText("注册失败,请稍后重试！");
            tip.setVisibility(View.VISIBLE);
            return;
        }
        register_result.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {
                int time = 3;
                while (time != 0) {
                    time--;
                    Message message = new Message();
                    message.what = 123;
                    message.arg1 = time;
                    handler.sendMessage(message);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "run: 222333");
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}

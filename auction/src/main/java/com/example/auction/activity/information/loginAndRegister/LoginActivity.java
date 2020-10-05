package com.example.auction.activity.information.loginAndRegister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.MainActivity;
import com.example.auction.R;
import com.example.auction.bean.UserInfo;
import com.example.auction.database.UserDAO;


public class LoginActivity extends Activity {

    EditText getID_edt, getPassword_edt;
    TextView login_tv, register_tv;
    int id;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        initView();
        initListener();
    }

    private void initView() {
        getID_edt = (EditText) findViewById(R.id.getID_edt);
        getPassword_edt = (EditText) findViewById(R.id.get_password_edt);
        login_tv = (TextView) findViewById(R.id.id_user_login);
        register_tv = (TextView) findViewById(R.id.to_register);
    }

    private void initListener() {
        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        });

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        id = Integer.parseInt(getID_edt.getText().toString());
        password = getPassword_edt.getText().toString();

        if (checkLogin()) {
            SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isLogin", true);
            editor.putInt("id", id);
            editor.commit();
            Toast.makeText(this, "登陆成功！", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private boolean checkLogin() {
        UserDAO userDAO = new UserDAO(this);
        UserInfo userInfo = userDAO.select(id);

        if (userInfo == null) {
            Toast.makeText(this, "用户不存在！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!userInfo.getPassword().equals(password)) {
            Toast.makeText(this, "密码不正确！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


}

package com.example.auction.activity.information.loginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.bean.UserInfo;
import com.example.auction.database.UserDAO;

public class RegisterActivity extends Activity {

    LinearLayout cancel;
    EditText name_edt, password_edt, email_edt;
    TextView register_tv;
    ImageView icon;
    String uri = null;
    String password, name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        init();
    }

    private void init() {
        initView();
        initListener();
    }

    private void initView() {
        cancel = (LinearLayout) findViewById(R.id.cancel);
        name_edt = (EditText) findViewById(R.id.set_name_edt);
        password_edt = (EditText) findViewById(R.id.set_password_edt);
        email_edt = (EditText) findViewById(R.id.set_email_edt);
        register_tv = (TextView) findViewById(R.id.id_user_register);
        icon = (ImageView) findViewById(R.id.set_user_icon);
    }

    private void initListener() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        //访问相册，得到图片
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 123);
            }
        });
    }

    private void register() {
        password = password_edt.getText().toString();
        name = name_edt.getText().toString();
        email = email_edt.getText().toString();

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入完整信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (uri == null) {
            Toast.makeText(this, "请选择头像！", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfo userInfo = new UserInfo(password, name, uri, email);
        UserDAO userDAO = new UserDAO(this);
        int id = userDAO.insert(userInfo);
        Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ShowMsgActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            uri = data.getData().toString();
            icon.setImageURI(Uri.parse(uri));
        }
    }
}

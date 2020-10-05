package com.example.chat;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chat.fragment.ContactsFragment;
import com.example.chat.fragment.MessagesFragment;
import com.example.chat.fragment.UserHomePageFragment;
import com.example.chat.loginAndRegister.LoginActivity;

public class MainActivity extends Activity {
    private android.app.FragmentManager fragmentManager;
    private Fragment fragment;
    private android.app.FragmentTransaction transaction;
    private ImageView image_message, image_contacts, image_homepage;
    private TextView text_message, text_contacts, text_homepage;
    LinearLayout to_msg, to_contacts, to_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initWindows();

        //判断是否登录，没有登录就跳转到登陆界面
        isLogin();

        //申请权限
        getPermission();
        //初始化
        initComponent();

        fragment = new MessagesFragment();
        loadFragment(R.id.content, fragment);
        text_message.setTextColor(Color.GREEN);
        image_message.setImageResource(R.drawable.msg_green);

        //点击底部，在三个fragment间跳转
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelection();
                switch (v.getId()) {
                    case R.id.to_msg_fra:
                        fragment = new MessagesFragment();
                        loadFragment(R.id.content, fragment);
                        text_message.setTextColor(Color.GREEN);
                        image_message.setImageResource(R.drawable.msg_green);
                        break;
                    case R.id.to_contacts_fra:
                        fragment = new ContactsFragment();
                        loadFragment(R.id.content, fragment);
                        text_contacts.setTextColor(Color.GREEN);
                        image_contacts.setImageResource(R.drawable.contacts_green);
                        break;
                    case R.id.to_home_fra:
                        fragment = new UserHomePageFragment();
                        loadFragment(R.id.content, fragment);
                        text_homepage.setTextColor(Color.GREEN);
                        image_homepage.setImageResource(R.drawable.homepage_green);
                        break;
                }
            }
        };

        to_msg.setOnClickListener(listener);
        to_home.setOnClickListener(listener);
        to_contacts.setOnClickListener(listener);


    }

    //初始化控件
    private void initComponent() {
        image_message = (ImageView) findViewById(R.id.image_message);
        image_contacts = (ImageView) findViewById(R.id.image_contacts);
        image_homepage = (ImageView) findViewById(R.id.image_homepage);
        text_message = (TextView) findViewById(R.id.text_message);
        text_contacts = (TextView) findViewById(R.id.text_contacts);
        text_homepage = (TextView) findViewById(R.id.text_homepage);
        to_home = (LinearLayout) findViewById(R.id.to_home_fra);
        to_msg = (LinearLayout) findViewById(R.id.to_msg_fra);
        to_contacts = (LinearLayout) findViewById(R.id.to_contacts_fra);
    }

    //加载布局
    public void loadFragment(int containerViewId, Fragment fragment) {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.commit();
    }

    //清楚底部的选中状体
    private void clearSelection() {
        image_message.setImageResource(R.drawable.msg_black);
        image_contacts.setImageResource(R.drawable.contacts_black);
        image_homepage.setImageResource(R.drawable.homepage_black);
        text_message.setTextColor(Color.parseColor("#82858b"));
        text_contacts.setTextColor(Color.parseColor("#82858b"));
        text_homepage.setTextColor(Color.parseColor("#82858b"));
    }

    //申请权限
    private void getPermission() {
        //申请读内存的权限
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
        //申请写内存的权限
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 122);
        }
    }

    //没有登录跳转到登录界面
    private void isLogin() {
        SharedPreferences sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if (!isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //设置沉浸式状态栏
    private void initWindows() {
        Window window = getWindow();
        //        int color = getResources().getColor(android.R.color.holo_blue_light);
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        //                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        //            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        //            window.setStatusBarColor(color);
        //设置导航栏颜色
        //            window.setNavigationBarColor(color);
        //            ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
        //            View childAt = contentView.getChildAt(0);
        //            if (childAt != null) {
        //                childAt.setFitsSystemWindows(true);
        //            }
        //        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        //透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //设置contentview为fitsSystemWindows
        //            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
        //            View childAt = contentView.getChildAt(0);
        //            if (childAt != null) {
        //                childAt.setFitsSystemWindows(true);
        //            }
        //给statusbar着色
        //            View view = new View(this);
        //            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        //            view.setBackgroundColor(color);
        //            contentView.addView(view);
        //        }
    }

    //    private int getStatusBarHeight() {
    //        Resources resources = this.getResources();
    //        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
    //        int height = resources.getDimensionPixelSize(resourceId);
    //        Log.v("dbw", "Status height:" + height);
    //        return height;
    //    }
}

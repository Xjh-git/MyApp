package com.example.auction;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.activity.information.loginAndRegister.LoginActivity;
import com.example.auction.fragment.AuctionFragment;
import com.example.auction.fragment.MessageFragment;
import com.example.auction.fragment.UserHomePageFragment;

public class MainActivity extends Activity {
    private android.app.FragmentManager fragmentManager;
    private Fragment fragment;
    private android.app.FragmentTransaction transaction;
    private ImageView image_auction, image_message, image_homepage;
    private TextView text_auction, text_message, text_homepage;
    LinearLayout to_auction, to_msg, to_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_main);

        getPermission();

        checkLogin();

        initComponent();

        fragment = new AuctionFragment();
        loadFragment(R.id.content, fragment);
        text_auction.setTextColor(Color.GREEN);
        image_auction.setImageResource(R.drawable.hammer_green);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelection();
                switch (v.getId()) {
                    case R.id.to_auction_fra:
                        fragment = new AuctionFragment();
                        loadFragment(R.id.content, fragment);
                        text_auction.setTextColor(Color.GREEN);
                        image_auction.setImageResource(R.drawable.hammer_green);
                        break;
                    case R.id.to_msg_fra:
                        fragment = new MessageFragment();
                        loadFragment(R.id.content, fragment);
                        text_message.setTextColor(Color.GREEN);
                        image_message.setImageResource(R.drawable.msg_green);
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

        to_auction.setOnClickListener(listener);
        to_home.setOnClickListener(listener);
        to_msg.setOnClickListener(listener);

    }

    private void initComponent() {
        image_auction = (ImageView) findViewById(R.id.image_acution);
        image_message = (ImageView) findViewById(R.id.image_message);
        image_homepage = (ImageView) findViewById(R.id.image_homepage);
        text_auction = (TextView) findViewById(R.id.text_auction);
        text_message = (TextView) findViewById(R.id.text_message);
        text_homepage = (TextView) findViewById(R.id.text_homepage);
        to_auction = (LinearLayout) findViewById(R.id.to_auction_fra);
        to_msg = (LinearLayout) findViewById(R.id.to_msg_fra);
        to_home = (LinearLayout) findViewById(R.id.to_home_fra);
    }

    public void loadFragment(int containerViewId, Fragment fragment) {
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(containerViewId, fragment);
        transaction.commit();
    }

    private void clearSelection() {
        image_auction.setImageResource(R.drawable.hammer_black);
        image_message.setImageResource(R.drawable.msg_black);
        image_homepage.setImageResource(R.drawable.homepage_black);
        text_auction.setTextColor(Color.parseColor("#82858b"));
        text_message.setTextColor(Color.parseColor("#82858b"));
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

    private void checkLogin() {
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin", false);
        if (!isLogin) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

}

package com.example.auction.activity.information.userInformation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.auction.R;
import com.example.auction.view.UpListView;

public class UserOrderActivity extends Activity {
    TextView back;
    UpListView orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_order);
        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initData() {

    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initView() {
        back=(TextView)findViewById(R.id.id_back);
    }
}

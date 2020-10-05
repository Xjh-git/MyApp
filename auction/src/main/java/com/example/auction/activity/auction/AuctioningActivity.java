package com.example.auction.activity.auction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.bean.Commodity;
import com.example.auction.database.CommodityDAO;

public class AuctioningActivity extends Activity {

    EditText edt_getName, edt_getDetails, edt_getCurrentPrice, edt_getMaxPrice;
    TextView tv_auction;
    String imgUri;
    ImageView img_getIcon;
    int loginID;
    RadioGroup auctionTime_rg;
    RadioButton timeOne_rbtn, timeTwo_rbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_auctioning);

        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initData() {
        //得到登陆客户的信息
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        loginID = sp.getInt("id", -1);
    }

    private void initListener() {
        //点击上架，上架拍卖品
        tv_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, details;
                double minPrice, maxPrice;
                name = edt_getName.getText().toString();
                details = edt_getDetails.getText().toString();
                minPrice = Double.valueOf(edt_getCurrentPrice.getText().toString());
                maxPrice = Double.valueOf(edt_getMaxPrice.getText().toString());

                //名称不能为空
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(AuctioningActivity.this, "请输入商品名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                //价格不能为空
                if (TextUtils.isEmpty(edt_getCurrentPrice.getText().toString())
                        || TextUtils.isEmpty(edt_getMaxPrice.getText().toString())) {
                    Toast.makeText(AuctioningActivity.this, "请输入价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                int auctionTime;
                if (timeOne_rbtn.isSelected()) {
                    auctionTime = 24;
                } else {
                    auctionTime = 48;
                }

                //插入数据库
                Commodity commodity = new Commodity(name, details, imgUri, maxPrice, minPrice, auctionTime, loginID);
                CommodityDAO commodityDAO = new CommodityDAO(AuctioningActivity.this);
                commodityDAO.insert(commodity);

                Toast.makeText(AuctioningActivity.this, "上架成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        //访问相册，得到图片
        img_getIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 123);
            }
        });
    }

    private void initView() {
        edt_getName = (EditText) findViewById(R.id.id_get_comm_name);
        edt_getDetails = (EditText) findViewById(R.id.id_get_comm_details);
        edt_getCurrentPrice = (EditText) findViewById(R.id.id_get_comm_minPrice);
        edt_getMaxPrice = (EditText) findViewById(R.id.id_get_comm_maxPrice);
        img_getIcon = (ImageView) findViewById(R.id.id_get_comm_icon);
        tv_auction = (TextView) findViewById(R.id.toAuction);
        auctionTime_rg = (RadioGroup) findViewById(R.id.auction_time);
        timeOne_rbtn = (RadioButton) findViewById(R.id.auction_time_one);
        timeTwo_rbtn = (RadioButton) findViewById(R.id.auction_time_two);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            imgUri = data.getData().toString();
            img_getIcon.setImageURI(Uri.parse(imgUri));
        }
    }
}

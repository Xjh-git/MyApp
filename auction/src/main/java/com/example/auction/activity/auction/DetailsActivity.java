package com.example.auction.activity.auction;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.bean.Commodity;
import com.example.auction.bean.Msg;
import com.example.auction.bean.UserInfo;
import com.example.auction.database.CommodityDAO;
import com.example.auction.database.MsgDAO;
import com.example.auction.database.UserDAO;

import java.util.Date;

public class DetailsActivity extends Activity {
    TextView tv_name, tv_details;
    Button btn_currentPrice, btn_maxPrice;
    ImageView img_icon;
    Commodity commodity;
    UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);

        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    //初始化监听器
    private void initListener() {
        //竞拍
        btn_currentPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(DetailsActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(DetailsActivity.this)
                        .setTitle("输入价格")
                        .setView(editText)
                        .setPositiveButton("确定", new DatePickerDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double price = Double.valueOf(editText.getText().toString());
                                double currPrice = commodity.getCurrentPrice();
                                if (price > currPrice) {
                                    //访问数据库，更新竞拍价
                                    auctioning(price, commodity);
                                } else {
                                    Toast.makeText(DetailsActivity.this, "请输入一个更高的价格", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        });


        //一口价
        btn_maxPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(DetailsActivity.this)
                        .setTitle("购买？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double price = commodity.getMaxPrice();
                                purchase(price);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }

    //购买商品
    private void purchase(double price) {
        //得到登陆客户的信息
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        int id = sp.getInt("id", -1);
        if (id == -1) {
            return;
        }
        UserDAO userDAO = new UserDAO(this);
        user = userDAO.select(id);

        if (user.getMoney() >= price) {
            double money = user.getMoney() - price;
            user.setMoney(money);
            userDAO.update(user);

            UserInfo seller = userDAO.select(commodity.getOwnerID());
            seller.setMoney(seller.getMoney() + commodity.getMaxPrice());
            userDAO.update(seller);
            Toast.makeText(this, "购买成功，花费：" + price, Toast.LENGTH_SHORT).show();

            Msg msg = new Msg(id, commodity.getOwnerID(), "来自系统的消息\n" +
                    "    您成功购买了来自用户 \"" + commodity.getOwnerID() + "\" 的商品 \"" + commodity.getName()
                    + "\" ，共花费" + price,
                    "", new Date(), false, false);
            MsgDAO msgDAO = new MsgDAO(this);
            msgDAO.sendMsg(msg);

            CommodityDAO commodityDAO = new CommodityDAO(this);
            commodityDAO.delete(commodity.getId());
        } else {
            Toast.makeText(this, "余额不足！", Toast.LENGTH_SHORT).show();
        }

    }

    private void auctioning(double price, Commodity commodity) {
        //更新数据库
        CommodityDAO commodityDAO = new CommodityDAO(this);

        commodity.setCurrentPrice(price);
        commodityDAO.update(commodity);

        btn_currentPrice.setText("竞拍价：" + price);
    }

    //初始化数据
    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        commodity = (Commodity) bundle.getSerializable("comm");

        tv_name.setText(commodity.getName());
        tv_details.setText(commodity.getDetails());
        btn_currentPrice.setText("竞拍价：" + commodity.getCurrentPrice());
        btn_maxPrice.setText("一口价：" + commodity.getMaxPrice());
        img_icon.setImageURI(Uri.parse(commodity.getIcon()));
    }

    //初始化控件
    private void initView() {
        tv_name = (TextView) findViewById(R.id.id_comm_name);
        tv_details = (TextView) findViewById(R.id.id_comm_details);
        btn_currentPrice = (Button) findViewById(R.id.id_comm_currentPrice);
        btn_maxPrice = (Button) findViewById(R.id.id_comm_maxPrice);
        img_icon = (ImageView) findViewById(R.id.id_comm_icon);
    }

}

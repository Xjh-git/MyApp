package com.example.auction.activity.information.userInformation;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auction.R;
import com.example.auction.bean.UserInfo;
import com.example.auction.database.UserDAO;

public class WithdrawalActivity extends Activity {

    EditText getWithdrawalMoney_edt;
    TextView withdrawal_tv;
    String money;
    UserInfo user;
    UserDAO userDAO;
    ImageView back;
    TextView limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        init();

    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        getWithdrawalMoney_edt = (EditText) findViewById(R.id.get_withdrawal_money);
        withdrawal_tv = (TextView) findViewById(R.id.withdrawal);
        back = (ImageView) findViewById(R.id.withdrawal_back);
        limit = (TextView) findViewById(R.id.withdrawal_limit);
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("LoginInfo", Context.MODE_PRIVATE);
        int id = sp.getInt("id", -1);
        if (id == -1) {
            return;
        }
        userDAO = new UserDAO(this);
        user = userDAO.select(id);

        limit.setText("提现金额（可提现金额：" + user.getMoney() + ")");

    }

    private void initListener() {
        getWithdrawalMoney_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                money = s.toString();
                if (!TextUtils.isEmpty(money)) {
                    withdrawal_tv.setBackgroundColor(0xff0391d9);
                } else {
                    withdrawal_tv.setBackgroundColor(0xffa2d1e9);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        withdrawal_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                money = getWithdrawalMoney_edt.getText().toString();
                if (!TextUtils.isEmpty(money)) {
                    double user_money = 0.0;
                    try {
                        user_money = Double.parseDouble(money);
                    } catch (NumberFormatException e) {
                        Toast.makeText(WithdrawalActivity.this, "请输入一个数字！", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    user.setMoney(user.getMoney() - user_money);
                    userDAO.update(user);
                    Toast.makeText(WithdrawalActivity.this, "提现成功！", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

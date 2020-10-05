package com.example.fuchaung;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.fuchaung.bean.QA;
import com.example.fuchaung.utils.QA_Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    List<HashMap<String, String>> common = new ArrayList<>();
    ;
    ListView common_lv;
    SimpleAdapter adapter;

    EditText getQuestion_edt;
    TextView search_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question = getQuestion_edt.getText().toString();
                if (question.equals(""))
                    return;
                List<QA> res = new QA_Test().queryList(question);
                common.clear();
                HashMap<String, String> hashMap;
                for (int i = 0; i < res.size(); i++) {
                    hashMap = new HashMap<>();
                    hashMap.put("问题", res.get(i).getStandardQ());
                    common.add(hashMap);
                }
                adapter.notifyDataSetChanged();
                getQuestion_edt.clearFocus();
            }
        });

        getQuestion_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getQuestion_edt.setSelectAllOnFocus(true);
                }
            }
        });

        getQuestion_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String question = s.toString();
                if (question.equals("")) {
                    initView();
                }
            }
        });
    }

    private void initView() {
        //申请写内存的权限
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 122);
        }
        //申请写内存的权限
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 122);
        }

        getQuestion_edt = (EditText) findViewById(R.id.getQuestion_edt);
        search_tv = (TextView) findViewById(R.id.search_tv);
        common_lv = (ListView) findViewById(R.id.common_question_lv);


        common.clear();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("问题", "转账失败的原因？");
        common.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("问题", "银证转账的时间？");
        common.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("问题", "信用账户可以购买ST股票吗？");
        common.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("问题", "新三版股票转让的计价单位？");
        common.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("问题", "新三板权限开通流程？");
        common.add(hashMap);

        adapter = new SimpleAdapter(this,
                common,
                R.layout.item_layout,
                new String[]{"问题"},
                new int[]{R.id.item_common_question});
        common_lv.setAdapter(adapter);
    }
}

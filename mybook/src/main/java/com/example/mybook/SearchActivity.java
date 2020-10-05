package com.example.mybook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mybook.bean.Novel;
import com.example.mybook.utils.BiQuGe;
import com.example.mybook.utils.NovelAdapter;
import com.example.mybook.utils.Website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {

    EditText bookName_edt;
    Button search_btn;
    Spinner website_sp;
    ProgressBar loading;


    List<Novel> data = new ArrayList<>();
    ListView novelList_lv;
    NovelAdapter adapter;

    String bookName;
    String web;
    Website website;

    TextView tip_tv;
    ProgressBar tip_pb;

    List<Novel> novelList;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123) {
                novelList = (List<Novel>) msg.obj;
                data.clear();
                data.addAll(novelList);
                adapter.notifyDataSetChanged();
                loading.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = bookName_edt.getText().toString();
                String website = website_sp.getSelectedItem().toString();
                Website web = null;
                switch (website) {
                    case "笔趣阁":
                        web = new BiQuGe();
                        break;
                    default:
                        web = new BiQuGe();
                        break;
                }

                search(web, name);
            }
        });


        novelList_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Novel novel = data.get(position);
                tip_tv.setVisibility(View.VISIBLE);
                tip_pb.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        website.getEBook(novel.getLink()).download();
                    }
                }.start();
            }
        });
    }

    private void init() {
        bookName_edt = (EditText) findViewById(R.id.book_name_edt_se);
        search_btn = (Button) findViewById(R.id.search_btn_se);
        website_sp = (Spinner) findViewById(R.id.website_sp_se);
        loading = (ProgressBar) findViewById(R.id.loading_pb_se);
        loading.setVisibility(View.GONE);

        novelList_lv = (ListView) findViewById(R.id.novel_lv);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bookName = bundle.getString("name");
        web = bundle.getString("website");
        switch (web) {
            case "笔趣阁":
                website = new BiQuGe();
                break;
            default:
                website = new BiQuGe();
                break;
        }
        //        data = (List<Novel>) bundle.getSerializable("novelList");
        adapter = new NovelAdapter(this, data);
        novelList_lv.setAdapter(adapter);

        tip_pb = (ProgressBar) findViewById(R.id.download_pb);
        tip_tv = (TextView) findViewById(R.id.download_tips_tv);
        tip_tv.setVisibility(View.GONE);
        tip_pb.setVisibility(View.GONE);
    }

    private void search(final Website website, final String bookName) {
        loading.setVisibility(View.VISIBLE);

        new Thread() {
            @Override
            public void run() {
                List<Novel> novels = website.search(bookName);
                Message message = new Message();
                message.obj = novels;
                message.what = 123;
                handler.sendMessage(message);
            }
        }.start();

    }
}

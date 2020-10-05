package com.example.mybook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mybook.bean.Novel;
import com.example.mybook.utils.BiQuGe;
import com.example.mybook.utils.Website;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends Activity {

    EditText bookName_edt;
    Button search_btn;
    Spinner website_sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = bookName_edt.getText().toString();
                String website = website_sp.getSelectedItem().toString();

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("website", website);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

    private void init() {
        bookName_edt = (EditText) findViewById(R.id.book_name_edt);
        search_btn = (Button) findViewById(R.id.search_btn);
        website_sp = (Spinner) findViewById(R.id.website_sp);
    }
}

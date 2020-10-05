package com.example.chat.activitices.newFriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chat.R;
import com.example.chat.activitices.ContactsDetailsActivity;

public class AddFriendActivity extends Activity {

    ImageView back_iv;
    TextView back_tv, search;
    EditText get_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friend);

        init();
    }

    private void init() {
        initView();
        initListener();
    }

    private void initView() {
        back_iv = (ImageView) findViewById(R.id.back_to_new_contacts_iv);
        back_tv = (TextView) findViewById(R.id.back_to_new_contacts_tv);
        search = (TextView) findViewById(R.id.search_contact_iv);
        get_username = (EditText) findViewById(R.id.get_contact_name_et);
    }

    private void initListener() {
        back_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = get_username.getText().toString();
                if (TextUtils.isEmpty(contactName)) {
                    return;
                }



                Intent intent = new Intent(AddFriendActivity.this, ContactsDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", contactName);
                bundle.putBoolean("isContacts",false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

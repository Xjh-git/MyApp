package com.example.auction.activity.information;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.auction.R;

public class EditInforActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_infor);
    }
}

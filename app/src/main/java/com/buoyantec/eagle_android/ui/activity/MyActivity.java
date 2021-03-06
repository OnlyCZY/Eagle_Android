package com.buoyantec.eagle_android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.buoyantec.eagle_android.ui.base.BaseActivity;

public class MyActivity extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private TextView name;
    private TextView phone;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        name = getViewById(R.id.my_name);
        phone = getViewById(R.id.my_phone);
    }

    @Override
    protected void setListener() {
        name.setText(sp.getString("name", null));
        phone.setText(sp.getString("phone", null));
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        subToolbarTitle.setText("账 号");
    }

}

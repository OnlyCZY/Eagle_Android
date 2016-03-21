package com.buoyantec.eagle_android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.StandardListAdapter;
import com.buoyantec.eagle_android.model.Device;
import com.buoyantec.eagle_android.model.Devices;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * 温湿度系统详情
 */
public class Temperature extends BaseActivity {
    private SharedPreferences sp;
    private Integer room_id;
    private String sub_sys_name;
    private Context context;

    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private CircleProgressBar circleProgressBar;
    private ListView listView;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temperature);
        Iconify.with(new FontAwesomeModule());
        // 初始化变量
        init();
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //初始化toolbar
        initToolbar();
        // 初始化list
        initListView();
    }

    private void init() {
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        room_id = sp.getInt("current_room_id", 1);
        sub_sys_name = getIntent().getStringExtra("sub_sys_name");
        context = this;
        // 组件
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        listView = getViewById(R.id.temperature_listView);
        circleProgressBar = getViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(sub_sys_name);
    }

    private void initListView() {
        // 获取指定链接数据
        mEngine.getDevices(room_id, sub_sys_name).enqueue(new Callback<Devices>() {
            @Override
            public void onResponse(Response<Devices> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> names = new ArrayList<>();
                    final ArrayList<Integer> ids = new ArrayList<>();
                    // 获取用户
                    List<Device> devices = response.body().getDevices();
                    for (Device device : devices) {
                        names.add(device.getName());
                        ids.add(device.getId());
                    }
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 图标
                    Integer image = R.drawable.system_status_temperature;

                    listView.setAdapter(new StandardListAdapter(listView, context, image, names));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            TextView title = (TextView) v.findViewById(R.id.list_item_standard_list_text);
                            Intent i = new Intent(Temperature.this, TemperatureDetail.class);
                            i.putExtra("title", title.getText());
                            i.putExtra("device_id", ids.get(position));
                            startActivity(i);
                        }
                    });
                    Log.i(sub_sys_name, context.getString(R.string.getSuccess) + code);
                } else {
                    // 输出非201时的错误信息
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i(sub_sys_name, context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i(sub_sys_name, context.getString(R.string.linkFailed));
            }
        });
    }
}
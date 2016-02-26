package com.buoyantec.eagle_android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.DeviceDetailListAdapter;
import com.buoyantec.eagle_android.myService.ApiRequest;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 当前: 系统状态 -> 电量仪系统 -> 详情
 */
public class MeterDetail extends AppCompatActivity {
    private CircleProgressBar circleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载字体图标
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_box_detail);
        //初始化toolbar
        initToolbar();
        //初始化list
        initListView();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        Intent i = getIntent();
        String title = i.getStringExtra("title");
        subToolbarTitle.setText(title);
    }

    private void initListView() {
        // 进度条
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.VISIBLE);

        // 获取device_id 和 room_id
        final SharedPreferences sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        Integer room_id = sp.getInt("current_room_id", 1);
        Intent i = getIntent();
        Integer device_id = i.getIntExtra("device_id", 1);
        final Context context = this;

        // 获取指定链接数据
        ApiRequest apiRequest = new ApiRequest(this);
        Call<LinkedHashMap<String, String>> call = apiRequest
                .getService()
                .getDeviceDataHash(room_id, device_id);
        call.enqueue(new Callback<LinkedHashMap<String, String>>() {
            @Override
            public void onResponse(Response<LinkedHashMap<String, String>> response) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                int code = response.code();
                if (code == 200) {
                    LinkedHashMap<String, String> map = response.body();
                    ArrayList<String> nameArray = new ArrayList<>();
                    ArrayList<String> statusArray = new ArrayList<>();

                    map.remove("id");
                    map.remove("name");
                    // 循环hash,存入数组
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        nameArray.add(entry.getKey());
                        statusArray.add(entry.getValue());
                    }

                    // item数据
                    String[] names = nameArray.toArray(new String[nameArray.size()]);
                    String[] status = statusArray.toArray(new String[statusArray.size()]);

                    // 加载列表
                    ListView listView = (ListView) findViewById(R.id.meter_detail_listView);
                    listView.setAdapter(new DeviceDetailListAdapter(listView, context, names, status));
                    Log.i("电量仪系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    Log.i("电量仪系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                Log.i("电量仪系统->详情", context.getString(R.string.linkFailed));
                // TODO: 16/2/22 错误处理
            }
        });
    }
}
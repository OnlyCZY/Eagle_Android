package com.buoyantec.eagle_android.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.buoyantec.eagle_android.adapter.TemperatureListAdapter;
import com.buoyantec.eagle_android.model.DeviceDetail;
import com.loopj.android.image.SmartImageView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class TemperatureDetail extends BaseActivity {
    private CircleProgressBar circleProgressBar;
    private SharedPreferences sp;
    private Toolbar toolbar;
    private SmartImageView myImage;
    private TextView subToolbarTitle;
    private ListView listView;
    private Context context;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temperature_detail);
        sp = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        myImage = getViewById(R.id.temperature_detail_image);
        listView = getViewById(R.id.temperature_detail_listView);
        circleProgressBar = getViewById(R.id.progressBar);
        context = this;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        // 初始化toolbar
        initToolbar();
        // 初始化list
        initListView();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText(getIntent().getStringExtra("title"));
    }

    private void initListView() {
        // 进度条
        circleProgressBar.setVisibility(View.VISIBLE);
        // 获取device_id 和 room_id
        Integer room_id = sp.getInt("current_room_id", 1);
        Integer device_id = getIntent().getIntExtra("device_id", 1);

        mEngine.getDeviceDataHash(room_id, device_id).enqueue(new Callback<DeviceDetail>() {
            @Override
            public void onResponse(Response<DeviceDetail> response) {
                int code = response.code();
                if (code == 200) {
                    ArrayList<String> tem = new ArrayList<>();
                    ArrayList<String> hum = new ArrayList<>();
                    ArrayList<String> temColor = new ArrayList<>();
                    ArrayList<String> humColor = new ArrayList<>();

                    // 获取图片
                    String path = response.body().getPic();
                    if (path == null || path.equals("null")) {
                        myImage.setBackgroundResource(R.drawable.device_default);
                    } else {
                        myImage.setImageUrl(path);
                    }
                    // 循环list,存入数组
                    List<HashMap<String, String>> points = response.body().getPoints();
                    for (HashMap<String, String> point : points) {
                        if (point.get("name").equals("温度")) {
                            tem.add(point.get("value"));
                            temColor.add(point.get("color"));
                        } else if (point.get("name").equals("湿度")) {
                            hum.add(point.get("value"));
                            humColor.add(point.get("color"));
                        }
                    }
                    // 隐藏进度条
                    circleProgressBar.setVisibility(View.GONE);

                    // 加载列表
                    listView.setAdapter(new TemperatureListAdapter(context, tem, temColor, hum, humColor));

                    Log.i("温湿度系统->详情", context.getString(R.string.getSuccess) + code);
                } else {
                    showToast(context.getString(R.string.getDataFailed));
                    Log.i("温湿度系统->详情", context.getString(R.string.getFailed) + code);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // 隐藏进度条
                circleProgressBar.setVisibility(View.GONE);
                showToast(context.getString(R.string.netWorkFailed));
                Log.i("温湿度系统->详情", context.getString(R.string.linkFailed));
            }
        });
    }
}

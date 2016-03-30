package com.buoyantec.eagle_android.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.buoyantec.eagle_android.model.PointAlarm;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Callback;
import retrofit2.Response;

public class ReceiverPush extends BaseActivity {
    private Toolbar toolbar;
    private TextView subToolbarTitle;
    private TextView deviceName;
    private TextView info;
    private TextView status;
    private TextView type;
    private TextView alarmTime;
    private TextView finishTime;
    private TextView user;
    private TextView confirmTime;
    private Button confirmButton;

    private Integer pointId;

    private SharedPreferences sp;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_receiver_push);
        toolbar = getViewById(R.id.sub_toolbar);
        subToolbarTitle = getViewById(R.id.sub_toolbar_title);
        // 告警设备名称(device_name)
        deviceName = getViewById(R.id.push_device_name);
        // 告警信息(comment)
        info = getViewById(R.id.push_alarm_info);
        // 状态(meaning)
        status = getViewById(R.id.push_alarm_status);
        // 类型(type)
        type = getViewById(R.id.push_alarm_type);
        // 告警时间(updated_at)
        alarmTime = getViewById(R.id.push_alarm_time);
        // 解除时间(checked_at)
        finishTime = getViewById(R.id.push_alarm_finish_time);
        // 操作员(checked_user)
        user = getViewById(R.id.push_alarm_user);
        // 确认时间
        confirmTime = getViewById(R.id.push_alarm_confirm_time);
        // 确认按钮
        confirmButton = getViewById(R.id.push_alarm_confirm_button);
        sp  = getSharedPreferences("foobar", Activity.MODE_PRIVATE);
    }

    @Override
    protected void setListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog("正在确认...");
                mEngine.checkAlarm(pointId).enqueue(new Callback<HashMap<String, String>>() {
                    @Override
                    public void onResponse(Response<HashMap<String, String>> response) {
                        HashMap<String, String> data = response.body();
                        if (data.get("result").equals("处理成功")) {
                            dismissLoadingDialog();
                            // 改变item操作员
                            user.setText(sp.getString("name", ""));
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            Date currentDate = new Date(System.currentTimeMillis());
                            String str = simpleDateFormat.format(currentDate);
                            confirmTime.setText(str);
                            showToast("确认成功");
                        } else {
                            dismissLoadingDialog();
                            showToast("确认失败");
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dismissLoadingDialog();
                        showToast("网络连接失败");
                    }
                });
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initToolBar();
        // 接收推送消息,更新UI
        setData();
    }

    private void setData() {
        // custom_content = {
        // "id":3210406,
        // "is_checked":true,
        // "updated_at":"2016-03-29T14:50:42.000Z",
        // "device_name":"APC 空调",
        // "alarm_value":"",
        // "state":0,
        // "created_at":"2016-03-02T08:39:57.000Z",
        // "pid":null,"meaning":"分",
        // "type":"digital",
        // "comment":"APC空调-开机状态",
        // "checked_at":null,
        // "point_id":4570
        // }
        String customContent = sp.getString("custom_content", null);
        System.out.println("receiverPush.customContent------->"+customContent);
        Gson gson = new Gson();
        PointAlarm alarm = gson.fromJson(customContent, PointAlarm.class);
        // 设置数据
        Log.d("alarm", "=============alarm===========");

        if (customContent != null) {
            if (!String.valueOf(alarm.getPointId()).isEmpty()) {
                pointId = alarm.getPointId();
                Log.d("pointId", String.valueOf(alarm.getPointId()));
            }
            if (alarm.getDeviceName() != null) {
                deviceName.setText(alarm.getDeviceName());
                Log.d("deviceName", alarm.getDeviceName());
            }
            if (alarm.getComment() != null) {
                info.setText(alarm.getComment());
                Log.d("info", alarm.getComment());
            }
            if (alarm.getMeaning() != null) {
                status.setText(alarm.getMeaning());
                Log.d("status", alarm.getMeaning());
            }
            if (alarm.getType() != null) {
                type.setText(alarm.getType());
                Log.d("type", alarm.getType());
            }
            if (alarm.getUpdatedAt() != null) {
                alarmTime.setText(alarm.getUpdatedAt());
                Log.d("alarmTime", alarm.getUpdatedAt());
            }

            if (!String.valueOf(alarm.getState()).isEmpty() && alarm.getUpdatedAt() != null) {
                if (alarm.getState() == 0) {
                    finishTime.setText(alarm.getUpdatedAt());
                } else {
                    finishTime.setText("");
                }
                Log.d("finishTime", alarm.getUpdatedAt());
            }
            if (alarm.getCheckedUser() != null) {
                user.setText(alarm.getCheckedUser());
                Log.d("user", alarm.getCheckedUser());
            }
            if (alarm.getCheckedAt() != null) {
                confirmTime.setText(alarm.getCheckedAt());
                Log.d("confirmTime", alarm.getCheckedAt());
            }
        }
    }

    private void initToolBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        subToolbarTitle.setText("推送告警信息");
    }
}

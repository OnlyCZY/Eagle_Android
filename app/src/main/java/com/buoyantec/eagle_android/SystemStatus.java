package com.buoyantec.eagle_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.buoyantec.eagle_android.API.MyService;
import com.buoyantec.eagle_android.adapter.SystemStatusGridAdapter;
import com.buoyantec.eagle_android.model.MySystem;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.SubSystem;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SystemStatus extends AppCompatActivity {
    private HashMap<String, Integer> systemIcon;
    private HashMap<String, ArrayList<String>> kindSystems;
    private Integer statusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_status);
        // sub_toolbar
        initToolbar();
        // 初始化systemIcon
        initSystemIcon();
        // 获取接口,刷新UI
        initSystems();
        //GridView
        initPowerSystemGrid();
        initEnvSystemGrid();
        initSafeSystemGrid();
    }

    //-------------------私有方法--------------------
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.sub_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView subToolbarTitle = (TextView) findViewById(R.id.sub_toolbar_title);
        subToolbarTitle.setText("系统状态");
    }

    private void initSystemIcon(){
        systemIcon = new HashMap<>();
        // 动力
        systemIcon.put("UPS系统", R.drawable.system_status_ups);
        systemIcon.put("电量仪系统", R.drawable.system_status_box);
        systemIcon.put("配电系统", R.drawable.system_status_power);
        systemIcon.put("电池检测", R.drawable.system_status_battery);
        systemIcon.put("发电机系统", R.drawable.system_status_engine);
        // 环境
        systemIcon.put("温湿度系统", R.drawable.system_status_temperature);
        systemIcon.put("机柜环境", R.drawable.system_status_cabinet);
        systemIcon.put("空调系统", R.drawable.system_status_air);
        systemIcon.put("漏水系统", R.drawable.system_status_water);
        // 联动
        // 安全
        systemIcon.put("消防系统", R.drawable.system_status_video);
        systemIcon.put("氢气检测", R.drawable.system_status_smoke);
        // 远程
        // 能效
        // 部署
    }

    //初始化动力系统GridView
    private void initPowerSystemGrid(){
        // references to our images
        Integer[] images = {
                R.drawable.system_status_power, R.drawable.system_status_ups,
                R.drawable.system_status_box, R.drawable.system_status_ats,
                R.drawable.system_status_battery, R.drawable.system_status_engine,
        };
        // texts of images
        String[] texts = { "配电", "UPS", "列头柜", "ATS", "蓄电池", "柴油机" };

        GridView gridview = (GridView) findViewById(R.id.grid_power_system);
        gridview.setAdapter(new SystemStatusGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            if (position == 0) {
                Intent i = new Intent(SystemStatus.this, PowerDistribution.class);
                startActivity(i);
            } else if (position == 1) {
                Intent i = new Intent(SystemStatus.this, UpsSystem.class);
                startActivity(i);
            } else if (position == 2) {
                Intent i = new Intent(SystemStatus.this, Box.class);
                startActivity(i);
            } else if (position == 3) {

            } else if (position == 4) {
                Intent i = new Intent(SystemStatus.this, Battery.class);
                startActivity(i);
            } else if (position == 5) {

            }
            }
        });
    }
    //初始化环境系统GridView
    private void initEnvSystemGrid(){
        // references to our images
        Integer[] images = {
                R.drawable.system_status_temperature, R.drawable.system_status_water,
                R.drawable.system_status_air, R.drawable.system_status_cabinet,
                R.drawable.system_status_empty, R.drawable.system_status_empty
        };
        // texts of images
        String[] texts = { "温湿度", "漏水", "精密空调", "机柜温度", "", "" };

        GridView gridview = (GridView) findViewById(R.id.grid_env_system);
        gridview.setAdapter(new SystemStatusGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(SystemStatus.this, Temperature.class);
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent(SystemStatus.this, Water.class);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(SystemStatus.this, PrecisionAir.class);
                    startActivity(i);
                } else if (position == 3) {
                    Intent i = new Intent(SystemStatus.this, Cabinet.class);
                    startActivity(i);
                } else if (position == 4) {

                } else if (position == 5) {

                }
            }
        });
    }
    //初始化安防系统GridView
    private void initSafeSystemGrid(){
        // references to our images
        Integer[] images = {
                R.drawable.system_status_video, R.drawable.system_status_door,
                R.drawable.system_status_smoke
        };
        // texts of images
        String[] texts = { "视频系统", "门禁系统", "烟感" };

        GridView gridview = (GridView) findViewById(R.id.grid_safe_system);
        gridview.setAdapter(new SystemStatusGridAdapter(gridview, this, images, texts));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(SystemStatus.this, VideoSystem.class);
                    startActivity(i);
                } else if (position == 1) {

                } else if (position == 2) {
                    Intent i = new Intent(SystemStatus.this, Smoke.class);
                    startActivity(i);
                }
            }
        });
    }

    /**
     * 如果用户身份合法,获取用户机房列表
     */
    private void initSystems() {
        final SharedPreferences sp = getSharedPreferences("foobar", MODE_PRIVATE);
        final String token = sp.getString("token", null);
        final String phone = sp.getString("phone", null);
        //定义拦截器,添加headers
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-User-Token", token)
                        .addHeader("X-User-Phone", phone)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();

        // 创建Retrofit实例
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://139.196.190.201/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        // 建立http请求
        MyService myService = retrofit.create(MyService.class);
        Call<MySystems> call = myService.getSystems();
        // 发送请求(使用同步加载)
        call.enqueue(new Callback<MySystems>() {
            @Override
            public void onResponse(Response<MySystems> response) {
                statusCode = response.code();
                if (response.body() != null && statusCode == 200) {
                    // 定义动态数组,用于保存子系统
                    ArrayList<String> subSystemList = new ArrayList<>();

                    //  获取所有的分类系统(比如: 动力,环境..)
                    List<MySystem> mySystems = response.body().getMySystems();
                    Iterator<MySystem> itr = mySystems.iterator();
                    while (itr.hasNext()) {
                        MySystem mySystem = itr.next();
                        String systemName = mySystem.getName();
                        System.out.println(systemName+"============");

                        // 获取所有的子系统( 比如: ups, 配电..)
                        List<SubSystem> subSystems = mySystem.getSubSystem();
                        Iterator<SubSystem> subItr = subSystems.iterator();
                        subSystemList.clear();
                        while (subItr.hasNext()) {
                            SubSystem subSystem = subItr.next();
                            String subName = subSystem.getSubSystemName();
                            System.out.println(subName);
                            subSystemList.add(subName);
                        }
                        // 把分类名和对应的子系统列表写入HashMap
                        kindSystems.put(systemName, subSystemList);
                    }

                    // 按照kindSystems加载UI

                } else {
                    try {
                        String error = response.errorBody().string();
                        System.out.println(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("system_status_status_code", statusCode);
                    editor.apply();
                    System.out.println(">>>>>>>>>>获取系统状态列表失败>>>>>>>>>>>>");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println(">>>>>>>>>>系统状态接口链接失败>>>>>>>>>>>>");
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("system_status_status_code", 2222);
                editor.apply();
            }
        });
        System.out.println(">>>>>>>>>>获取系统状态列表执行完成>>>>>>>>>>>>");
    }
}

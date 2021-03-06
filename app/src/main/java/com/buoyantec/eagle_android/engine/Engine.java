package com.buoyantec.eagle_android.engine;

import com.buoyantec.eagle_android.model.Alarm;
import com.buoyantec.eagle_android.model.DeviceDetail;
import com.buoyantec.eagle_android.model.Devices;
import com.buoyantec.eagle_android.model.PointAlarm;
import com.buoyantec.eagle_android.model.RoomAlarm;
import com.buoyantec.eagle_android.model.Rooms;
import com.buoyantec.eagle_android.model.MySystems;
import com.buoyantec.eagle_android.model.User;

import java.util.HashMap;

import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by kang on 16/1/25.
 * service: http://139.196.190.201
 */
public interface Engine {
    // 获取用户信息 status: 201
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("users/sign_in")
    Call<User> getUser(@Field("user[phone]") String phone, @Field("user[password]") String password);

    // 上传用户token
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("users/update_device")
    Call<User> upLoadDeviceToken(@Field("user[os]") String os, @Field("user[device_token]") String device_token);

    // 获取验证码
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("sms_tokens")
    Call<HashMap<String, String>> getSms(@Field("sms_token[phone]") String phone);

    // 修改密码
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("users/update_password")
    Call<User> updatePassword(@Field("user[phone]") String phone, @Field("user[password]") String password, @Field("user[sms_token]") String sms);

    // 获取机房列表 status: 200
    @Headers("Accept: application/json")
    @GET("rooms")
    Call<Rooms> getRooms();

    // 获取系统列表 status: 200
    @Headers("Accept: application/json")
    @GET("systems")
    Call<MySystems> getSystems(@Query("room_id") Integer room_id);

    // 根据子系统获取设备, 返回: device类
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("rooms/{id}/devices/search")
    Call<Devices> getDevices(@Path("id") Integer room_id, @Field("sub_sys_name") String sub_sys_name);


    // 根据设备id获取设备信息 status: 200, 返回hashMap
    @Headers("Accept: application/json")
    @GET("rooms/{room_id}/devices/{id}?api_version=2")
    Call<DeviceDetail> getDeviceDataHash(@Path("room_id") Integer room_id, @Path("id") Integer device_id);


    // 根据子系统id获取子系统告警列表 status: 200
    @Headers("Accept: application/json")
    @GET("rooms/{room_id}/sub_systems/{sub_system_id}/point_alarms")
    Call<Alarm> getWarnMessages(@Path("room_id") Integer device_id,
                                @Path("sub_system_id") Integer sub_system_id,
                                @Query("checked") Integer checked,
                                @Query("page") Integer page);

    // 获取机房下子系统的告警数 status: 200
    @Headers("Accept: application/json")
    @GET("rooms/{room_id}/point_alarms/count")
    Call<RoomAlarm> getSubSystemAlarmCount(@Path("room_id") Integer room_id);

    // 获取子系统下设备的告警数 status: 200
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("rooms/{room_id}/point_alarms/count")
    Call<RoomAlarm> getDeviceAlarmCount(@Path("room_id") Integer room_id, @Field("sub_system_id") Integer sub_system_id);

    // 获取告警详情
    @Headers("Accept: application/json")
    @GET("point_alarms/{id}")
    Call<PointAlarm> getAlarm(@Path("id") Integer id);

    // 确认告警
    @Headers("Accept: application/json")
    @POST("point_alarms/{id}/checked")
    Call<HashMap<String, String>> checkAlarm(@Path("id") Integer id);

}

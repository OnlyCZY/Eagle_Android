package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kang on 16/2/7.
 */


public class Devices {
    // 转化为POJO
    @SerializedName("devices")
    @Expose
    private List<Device> devices = new ArrayList<Device>();

    /**
     *
     * @return
     * The devices
     */
    public List<Device> getDevices() {
        return devices;
    }

}

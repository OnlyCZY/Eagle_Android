package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MySystem {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("sub_system")
    @Expose
    private List<SubSystem> subSystem = new ArrayList<SubSystem>();

    /**
     * No args constructor for use in serialization
     *
     */
    public MySystem() {
    }

    /**
     *
     * @param id
     * @param name
     * @param subSystem
     */
    public MySystem(int id, String name, List<SubSystem> subSystem) {
        this.id = id;
        this.name = name;
        this.subSystem = subSystem;
    }

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     * The subSystem
     */
    public List<SubSystem> getSubSystem() {
        return subSystem;
    }
}
package com.buoyantec.iGrid.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Results {

    @SerializedName("results")
    @Expose
    private List<Result> results = new ArrayList<Result>();

    /**
     *
     * @return
     * The results
     */
    public List<Result> getResults() {
        return results;
    }

}
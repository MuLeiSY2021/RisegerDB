package org.riseger.protoctl.compiler.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultSet {
    Map<String, ResultModelSet> modelSetMap = new HashMap<>();

    int size = 0;

    public int getCount() {
        return modelSetMap.size();
    }

    public static ResultSet empty() {
        return new ResultSet();
    }


    public ResultModelSet getModelSet(String model) {
        return modelSetMap.get(model);
    }

    public void setModelSet(String model, ResultModelSet modelSet) {
        this.modelSetMap.put(model, modelSet);
    }

    public boolean hasModelSet(String model) {
        return modelSetMap.containsKey(model);
    }

    public void pack() {
        for (ResultModelSet modelSet : modelSetMap.values()) {
            size += modelSet.size();
        }
    }
}

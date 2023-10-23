package org.riseger.protoctl.search.result;

import java.util.HashMap;
import java.util.Map;

public class ResultSet {
    Map<String, ResultModelSet> modelSetMap = new HashMap<>();


    public ResultModelSet getModelSet(String model) {
        return modelSetMap.get(model);
    }

    public void setModelSet(String model, ResultModelSet modelSet) {
        this.modelSetMap.put(model, modelSet);
    }

    public boolean hasModelSet(String model) {
        return modelSetMap.containsKey(model);
    }
}

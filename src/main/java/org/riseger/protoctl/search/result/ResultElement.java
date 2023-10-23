package org.riseger.protoctl.search.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultElement {
    private Double[][] keyColumns;

    private Map<String, Object> columns = new HashMap<String, Object>();

    public Object getColumn(String key) {
        return this.columns.get(key);
    }

    public void addColumn(String column, Object o) {
        this.columns.put(column, o);
    }
}

package org.riseger.protoctl.compiler.result;

import java.util.HashMap;
import java.util.Map;

public class ResultElement {
    private final Map<String, Object> columns = new HashMap<>();
    private final Map<Integer, Double[]> keyColumns = new HashMap<>();

    public Object getColumn(String key) {
        return this.columns.get(key);
    }

    public void addColumn(String column, Object o) {
        this.columns.put(column, o);
    }

    public void setAllKeyColumns(Double[][] coordsSet) {
        for (int i = 0; i < coordsSet.length; i++) {
            for (int j = 0; j < coordsSet[i].length; j++) {
                setKeyColumns(i, j, coordsSet[i][j]);
            }
        }
    }

    public void setKeyColumns(int index, int x, double value) {
        Double[] coord;
        if (keyColumns.containsKey(index)) {
            coord = keyColumns.get(index);
        } else {
            coord = new Double[2];
            keyColumns.put(index, coord);
        }
        coord[x] = value;
    }

    public void setKeyColumns(int index, Double[] value) {
        Double[] coord;
        if (keyColumns.containsKey(index)) {
            coord = keyColumns.get(index);
        } else {
            coord = new Double[2];
            keyColumns.put(index, coord);
        }
        coord[0] = value[0];
        coord[1] = value[1];
    }
}

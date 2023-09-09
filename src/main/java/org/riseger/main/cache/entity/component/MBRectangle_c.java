package org.riseger.main.cache.entity.component;

import lombok.Getter;
import pers.muleisy.rtree.rectangle.MBRectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Getter
public class MBRectangle_c extends MBRectangle {

    private Double[][] coordsSet;

    public MBRectangle_c(ConcurrentMap<String, String> map, double threshold) {
        super(threshold);
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        if (map == null || map.isEmpty()) {
            return;
        }
        Map<Integer, Double[]> coordsSet = new HashMap<>();
        for (Map.Entry<String, String> a : map.entrySet()) {
            String k = a.getKey();
            if (k.startsWith("KEY")) {
                String[] keys = k.split("::");
                Integer i = Integer.parseInt(keys[1]);
                String x_y = keys[2];
                Double[] coords;

                if (!coordsSet.containsKey(i)) {
                    coords = new Double[2];
                    coordsSet.put(i, coords);
                } else {
                    coords = coordsSet.get(i);
                }

                double v = super.truncateDecimal(Double.parseDouble(a.getValue()));
                if (x_y.equals("x")) {
                    coords[0] = v;
                    minX = Math.min(minX, v);
                    maxX = Math.max(maxX, v);
                } else if (x_y.equals("y")) {
                    coords[1] = v;
                    minY = Math.min(minY, v);
                    maxY = Math.max(maxX, v);
                }
                map.remove(k);
            }
        }
        this.coordsSet = new Double[coordsSet.size()][];
        for (Map.Entry<Integer, Double[]> entry : coordsSet.entrySet()) {
            this.coordsSet[entry.getKey()] = entry.getValue();
        }
        super.setMinX(minX);
        super.setMinY(minY);
        super.setMaxX(maxX);
        super.setMaxY(maxY);
    }
}

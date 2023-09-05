package org.riseger.main.cache.entity.component;

import lombok.Getter;
import pers.muleisy.rtree.rectangle.MBRectangle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Getter
public class MBRectangle_c extends MBRectangle {

    private final List<Double> xKeySet = new LinkedList<>();

    private final List<Double> yKeySet = new LinkedList<>();

    private Double[][] coordsSet;

    public MBRectangle_c(ConcurrentMap<String, String> map, double threshold) {
        super(threshold);
        if (map == null || map.isEmpty()) {
            return;
        }
        Map<Integer, Double[]> coordsSet = new HashMap<>();
        for (Map.Entry<String, String> a : map.entrySet()) {
            String k = a.getKey();
            if (k.startsWith("KEY")) {
                String[] keys = k.split("::");
                Integer i = Integer.parseInt(keys[1]);
                Double[] coords;

                if (!coordsSet.containsKey(i)) {
                    coords = new Double[2];
                    coordsSet.put(i, coords);
                } else {
                    coords = coordsSet.get(i);
                }

                double v = super.truncateDecimal(Double.parseDouble(a.getValue()));
                if (keys[1].equals("x")) {
                    addX(v);
                    coords[0] = v;
                } else if (keys[1].equals("y")) {
                    addY(v);
                    coords[1] = v;
                }
                map.remove(k);
            }
        }
        this.coordsSet = new Double[coordsSet.size()][];
        for (Map.Entry<Integer, Double[]> entry : coordsSet.entrySet()) {
            this.coordsSet[entry.getKey()] = entry.getValue();
        }
    }

    private void addX(double x) {
        for (int i = 0; i < xKeySet.size(); i++) {
            if (x < xKeySet.get(i)) {
                xKeySet.add(i, x);
                return;
            }
        }
        xKeySet.add(x);
    }

    private void addY(double y) {
        for (int i = 0; i < yKeySet.size(); i++) {
            if (y < yKeySet.get(i)) {
                yKeySet.add(i, y);
                return;
            }
        }
        yKeySet.add(y);
    }
}

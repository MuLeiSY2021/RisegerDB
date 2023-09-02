package org.riseger.main.cache.entity.component.mbr;

import pers.muleisy.rtree.rectangle.MBRectangle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MBRectangle_c extends MBRectangle {

    private final List<Double> xKeySet = new LinkedList<>();

    private final List<Double> yKeySet = new LinkedList<>();

    private final ArrayList<Double[]> coordsSet = new ArrayList<>();

    public MBRectangle_c(Map<String,String> map, double threshold) {
        super(threshold);
        if(map == null || map.isEmpty()) {
//            LogManager.getLogManager().getLogger(this.getClass().getName()).warning("MBRectangle_c: map is null or empty");
            return;
        }
        for (Map.Entry<String,String> a : map.entrySet()) {
            String k = a.getKey();
            if(k.startsWith("KEY")) {
                String[] keys = k.split("::");
                int i = Integer.parseInt(keys[0]);
                Double[] coords;
                if(coordsSet.get(i) != null) {
                    coords = new Double[2];
                    coordsSet.add(i, coords);
                } else {
                    coords = coordsSet.get(i);
                }

                double v = super.truncateDecimal(Double.parseDouble(a.getValue()));
                if(keys[1].equals("x")) {
                    addX(v);
                    coords[0] = v;
                }else if(keys[1].equals("y")) {
                    addY(v);
                    coords[1] = v;
                }
                map.remove(k);
            }
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

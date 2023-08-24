package org.riseger.main.cache.entity.element;

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
            }
        }
    }

    @Override
    public void initBMRCoords() {
        setMinX(xKeySet.get(xKeySet.size() - 1));
        setMaxX(xKeySet.get(0));
        setMinY(yKeySet.get(yKeySet.size() - 1));
        setMaxY(yKeySet.get(0));
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

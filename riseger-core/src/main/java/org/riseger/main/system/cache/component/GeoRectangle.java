package org.riseger.main.system.cache.component;

import lombok.Getter;
import pers.muleisy.rtree.rectangle.MBRectangle;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Getter
public class GeoRectangle extends MBRectangle implements KeyComponent {

    private final Double[][] coordsSet;


    public GeoRectangle(Double threshold) {
        super(threshold);
        this.coordsSet = new Double[2][2];
    }

    public GeoRectangle(ConcurrentMap<String, String> map, double threshold) {
        super(threshold);
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
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
                    maxY = Math.max(maxY, v);
                }
                map.remove(k);
            }
        }
        this.coordsSet = new Double[coordsSet.size()][2];
        for (Map.Entry<Integer, Double[]> entry : coordsSet.entrySet()) {
            this.coordsSet[entry.getKey()] = entry.getValue();
        }
        super.setMinX(minX);
        super.setMinY(minY);
        super.setMaxX(maxX);
        super.setMaxY(maxY);
    }

    public GeoRectangle(Coords coords, double threshold) {
        super(coords.toRects(threshold), threshold);
        this.coordsSet = coords.toDoubleArray();
    }

    public GeoRectangle(Coord coord, Double threshold) {
        super(threshold, coord.rectangle(threshold));
        coordsSet = new Double[2][2];
        coordsSet[0][0] = minX();
        coordsSet[0][1] = minY();
        coordsSet[1][0] = maxX();
        coordsSet[1][1] = maxY();
    }

    public GeoRectangle(Coord coord, Double len, Double threshold) {
        super(threshold);
        super.setMaxX(coord.getX() + len);
        super.setMinX(coord.getX() - len);
        super.setMaxY(coord.getY() + len);
        super.setMinY(coord.getY() - len);
        coordsSet = new Double[2][2];
        coordsSet[0][0] = minX();
        coordsSet[0][1] = minY();
        coordsSet[1][0] = maxX();
        coordsSet[1][1] = maxY();
    }

    @Override
    public void expand(Rectangle rectangle) {
        super.expand(rectangle);
        coordsSet[0][0] = minX();
        coordsSet[0][1] = minY();
        coordsSet[1][0] = maxX();
        coordsSet[1][1] = maxY();
    }


    @Override
    public GeoRectangle toRectangle(double threshold) {
        return this;
    }

    protected void resetIndex(Rectangle rectangle) {
        super.setMaxX(rectangle.maxX());
        super.setMinX(rectangle.minX());
        super.setMaxY(rectangle.maxY());
        super.setMinY(rectangle.minY());
    }
}

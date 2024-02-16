package org.riseger.main.system.cache.entity.component;

import lombok.Getter;
import org.riseger.main.system.cache.entity.Entity;
import org.riseger.main.system.cache.entity.Entity_f;
import pers.muleisy.rtree.rectangle.MBRectangle;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Getter
public class MBRectangle_c extends MBRectangle implements Entity_f {

    private final Double[][] coordsSet;

    private final Entity_f entity = new Entity();

    public MBRectangle_c(Double threshold) {
        super(threshold);
        this.coordsSet = new Double[2][2];
    }

    public MBRectangle_c(ConcurrentMap<String, String> map, double threshold) {
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

    public MBRectangle_c(Coord_c coord, Double len, Double threshold) {
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
    public void changeEntity() {
        entity.changeEntity();
    }

    @Override
    public void resetChanged() {
        entity.resetChanged();
    }
}

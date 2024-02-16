package org.riseger.main.system.cache.entity.component;

import org.riseger.main.system.cache.entity.Entity;

public class Coord_c extends Entity {
    private final Double[] coordinates = new Double[2];

    public Coord_c(double x, double y) {
        this.coordinates[0] = x;
        this.coordinates[1] = y;
    }

    public double distance(Coord_c c2) {
        double x = Math.abs(this.coordinates[0] - c2.coordinates[0]),
                y = Math.abs(this.coordinates[1] - c2.coordinates[1]);
        double mn = Math.min(x, y);
        return (x + y) - (mn / 2) - (mn / 4) + (mn / 16);
    }

    public Double getX() {
        return coordinates[0];
    }

    public Double getY() {
        return coordinates[1];
    }
}

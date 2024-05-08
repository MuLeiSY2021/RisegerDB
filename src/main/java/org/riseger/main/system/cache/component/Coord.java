package org.riseger.main.system.cache.component;

import lombok.Getter;
import pers.muleisy.rtree.rectangle.Rectangle;

@Getter
public class Coord implements KeyComponent {
    private final Double[] coordinates = new Double[2];

    public Coord(double x, double y) {
        this.coordinates[0] = x;
        this.coordinates[1] = y;
    }

    public double distance(Coord c2) {
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

    public Rectangle rectangle(double threshold) {
        return new GeoRectangle(this, threshold);
    }

    @Override
    public GeoRectangle toRectangle(double threshold) {
        return new GeoRectangle(this, threshold);
    }
}

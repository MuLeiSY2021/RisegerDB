package org.riseger.main.system.cache.component;

import lombok.Getter;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.util.LinkedList;
import java.util.List;

@Getter
public class Coords implements KeyComponent {
    private final List<Coord> coords = new LinkedList<>();

    public Coords() {
    }

    public void add(Coord coord) {
        coords.add(coord);
    }

    public List<Rectangle> toRects(double threshold) {
        List<Rectangle> rects = new LinkedList<>();
        for (Coord coord : coords) {
            rects.add(new GeoRectangle(coord, threshold));
        }
        return rects;
    }

    public Double[][] toDoubleArray() {
        Double[][] array = new Double[this.coords.size()][2];
        for (int i = 0; i < coords.size(); i++) {
            array[i] = coords.get(i).getCoordinates();
        }
        return array;
    }

    @Override
    public GeoRectangle toRectangle(double threshold) {
        return new GeoRectangle(this, threshold);
    }
}

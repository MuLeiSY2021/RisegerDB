package pers.muleisy.rtree.test;

import pers.muleisy.rtree.rectangle.MBRectangle;

@Deprecated
public class TestRectangle extends MBRectangle {
    public TestRectangle(Double minX, Double maxX, Double minY, Double maxY, Double threshold) {
        super(threshold);
        setMaxX(maxX);
        setMinX(minX);
        setMaxY(maxY);
        setMinY(minY);
    }
}

package pers.muleisy.rtree.rectangle;

import java.util.Collection;
import java.util.List;

public interface Rectangle {

    Double maxX();

    Double maxY();

    Double minX();

    Double minY();

    void expand(Rectangle rectangle);

    boolean expandAll(Collection<? extends Rectangle> rects);

    boolean expandAll(Rectangle... rects);

    void copy(Rectangle rectangle);

    Double area();

    Rectangle overlap(Rectangle rectangle);

    boolean isLegal();

    Double margin();

    Double distance(Rectangle rect);

    boolean match(Rectangle rect);

    boolean inner(Rectangle rect);

    boolean intersects(Rectangle other);

    boolean intersects(List<? extends Rectangle> other);

    Double width();

    Double height();

    void move(Double x, Double y);
}

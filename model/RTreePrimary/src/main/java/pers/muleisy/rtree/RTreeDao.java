package pers.muleisy.rtree;

import pers.muleisy.rtree.rectangle.MBRectangle;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.util.List;

public interface RTreeDao<R extends MBRectangle> {

    void insert(R rectangle);

    int delete(Rectangle rectangle);

    void deleteStrict(Rectangle rectangle);

    List<R> search(Rectangle rectangle);

    R selectStrict(Rectangle rectangle);

    void insertAll(List<? extends R> rects);

    List<MBRectangle> getAllNode4Test();

    int[] getMapSize();

    int getDeep();

}

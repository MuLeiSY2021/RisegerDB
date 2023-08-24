package pers.muleisy.rtree;

import pers.muleisy.rtree.rectangle.CommonRectangle;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.util.List;

public interface RTreeDao<R extends CommonRectangle> {

    void insert(R rectangle);

    int delete(Rectangle rectangle);

    void deleteStrict(Rectangle rectangle);

    List<R> search(Rectangle rectangle);

    R selectStrict(Rectangle rectangle);

    void insertAll(List<? extends R> rects);

    List<CommonRectangle> getAllNode4Test();

    int[] getMapSize();

    int getDeep();

}

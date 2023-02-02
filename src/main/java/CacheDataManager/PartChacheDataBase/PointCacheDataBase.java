package CacheDataManager.PointDataBase;

import CacheDataManager.CacheDataBase;
import CacheDataManager.MapDataBase.MapCacheDataBase;
import StoredDataManager.DataStruct.GeoBaseStruct.GeoStruct;
import StoredDataManager.DataStruct.GeoBaseStruct.Point;
import StoredDataManager.DataStruct.R_Tree.R_Tree;
import StoredDataManager.DataStruct.R_Tree.Rectangle;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PointCacheDataBase extends CacheDataBase {
    private MapCacheDataBase mapCacheDataBase;

    private final ConcurrentHashMap<String, Point> pointMap = new ConcurrentHashMap<>();

    public PointCacheDataBase(MapCacheDataBase mapCacheDataBase, R_Tree<GeoStruct> tree) {
        super(tree);
        this.mapCacheDataBase = mapCacheDataBase;
    }

    public Point getPoint(String key) {
        return pointMap.get(key);
    }

    public LinkedList<Point> getPoints(Rectangle rectangle) {
        LinkedList<Point> res = new LinkedList<>();
        LinkedList<GeoStruct> selected = super.getGeoStruct(rectangle);
        for (GeoStruct geoStruct: selected) {
            if(geoStruct instanceof Point) {
                res.add((Point) geoStruct);
            }
        }
        return res;
    }
}

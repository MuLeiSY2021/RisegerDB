package CacheDataManager.MapDataBase;

import CacheDataManager.CacheDataBase;
import CacheDataManager.FieldDataBase.FieldCacheDataBase;
import CacheDataManager.PointDataBase.PointCacheDataBase;
import CacheDataManager.RoadDataBase.RoadCacheDataBase;
import StoredDataManager.DataStruct.GeoBaseStruct.Point;
import StoredDataManager.DataStruct.R_Tree.R_Tree;

public class MapCacheDataBase extends CacheDataBase {
    private final String key;

    private FieldCacheDataBase fieldCacheDataBase;

    private PointCacheDataBase pointCacheDataBase;

    private RoadCacheDataBase roadCacheDataBase;

    public MapCacheDataBase(String key) {
        super(new R_Tree<>());
        this.key = key;
        this.fieldCacheDataBase = new FieldCacheDataBase(this,super.getGeoStructR_tree());
        this.pointCacheDataBase = new PointCacheDataBase(this,super.getGeoStructR_tree());
        this.roadCacheDataBase = new RoadCacheDataBase(this,super.getGeoStructR_tree());
    }

    public Point getPoint(String key) {
        return pointCacheDataBase.
    }
}

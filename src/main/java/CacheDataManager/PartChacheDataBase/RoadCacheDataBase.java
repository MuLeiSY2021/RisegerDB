package CacheDataManager.RoadDataBase;

import CacheDataManager.CacheDataBase;
import CacheDataManager.MapDataBase.MapCacheDataBase;
import StoredDataManager.DataStruct.GeoBaseStruct.GeoStruct;
import StoredDataManager.DataStruct.GeoBaseStruct.Road;
import StoredDataManager.DataStruct.R_Tree.R_Tree;

import java.util.concurrent.ConcurrentHashMap;

public class RoadCacheDataBase extends CacheDataBase {
    private MapCacheDataBase mapCacheDataBase;

    private final ConcurrentHashMap<String ,Road> roadMap = new ConcurrentHashMap<>();

    public RoadCacheDataBase(MapCacheDataBase mapCacheDataBase, R_Tree<GeoStruct> tree) {
        super(tree);
        this.mapCacheDataBase = mapCacheDataBase;
    }
}

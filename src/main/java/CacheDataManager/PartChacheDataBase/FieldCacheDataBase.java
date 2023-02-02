package CacheDataManager.FieldDataBase;

import CacheDataManager.CacheDataBase;
import CacheDataManager.MapDataBase.MapCacheDataBase;
import StoredDataManager.DataStruct.GeoBaseStruct.Field;
import StoredDataManager.DataStruct.GeoBaseStruct.GeoStruct;
import StoredDataManager.DataStruct.R_Tree.R_Tree;

import java.util.concurrent.ConcurrentHashMap;

public class FieldCacheDataBase extends CacheDataBase {
    private MapCacheDataBase mapCacheDataBase;

    private final ConcurrentHashMap<String, Field> fieldMap = new ConcurrentHashMap<>();

    public FieldCacheDataBase(MapCacheDataBase mapCacheDataBase, R_Tree<GeoStruct> tree) {
        super(tree);
        this.mapCacheDataBase = mapCacheDataBase;
    }


}

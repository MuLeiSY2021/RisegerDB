package org.riseger.main.storage;

import com.google.gson.reflect.TypeToken;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.builder.MapInitBuilder;
import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.utils.Utils;

import java.io.File;
import java.util.*;

public class FileSystemManagers {
    public static FileSystemManagers DEFAULT;
    public String rootPath;
    public PreloadFSM preloadFSM;


    public FileSystemManagers(String rootPath) {
        this.rootPath = rootPath;
        this.preloadFSM = new PreloadFSM(rootPath);
    }

    public List<Database_c> initDatabases() {
        List<Database_c> databases = new LinkedList<>();
        File root = new File(this.rootPath + "/data/databases");

        if (root.exists()) {
            for (File db_ : root.listFiles()) {
                if (db_.isDirectory() && db_.getName().endsWith(Constant.DATABASE_PREFIX)) {
                    databases.add(getDatabase(db_));
                }
            }
        }
        return databases;
    }

    public Database_c getDatabase(File db_) {
        Database_c db = new Database_c(db_.getName().split("\\.")[0]);
        for (File map_ : db_.listFiles()) {
            if (map_.isDirectory() && map_.getName().endsWith(Constant.MAP_PREFIX)) {
                db.initMap(getMap(map_, db));
            }
        }
        return db;
    }

    public MapDB_c getMap(File map_, Database_c db) {
        MapInitBuilder mb = new MapInitBuilder();
        mb.setDatabase(db);
        mb.setName(map_.getName().split("\\.")[0]);
        for (File submap_ : Objects.requireNonNull(map_.listFiles())) {
            if (submap_.getName().endsWith(Constant.LAYER_PREFIX)) {
                mb.addLayer(submap_);
            } else if (submap_.getName().equals(Constant.CONFIG_FILE_NAME
                    + Constant.DOT_PREFIX
                    + Constant.JSON_PREFIX)) {
                Map<String, Config> configs = (Map<String, Config>) JsonSerializer.deserialize(Utils.getText(submap_),
                        TypeToken.getParameterized(HashMap.class, String.class, Config.class));
                mb.setConfigs(configs);
            }
        }

        return mb.build();
    }

}

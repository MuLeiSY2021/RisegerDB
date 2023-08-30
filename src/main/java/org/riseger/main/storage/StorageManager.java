package org.riseger.main.storage;

import org.riseger.main.cache.entity.builder.MapBuilder;
import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.storage.init.StorageInitSession;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StorageManager {
    public StorageInitSession initCache() {
        return new StorageInitSession();
    }

    private class InitCacheRunnable implements Runnable{
        private final StorageInitSession initSession;

        private String rootUri;

        public InitCacheRunnable(StorageInitSession initSession, String rootUri) {
            this.initSession = initSession;
            this.rootUri = rootUri;
        }

        @Override
        public void run() {
            File rootDir = new File(rootUri);
            for (File db_f:rootDir.listFiles()) {
                if(db_f.getName().endsWith("_db")) {
                    Database_c db = new Database_c(db_f.getName().split("_db")[0]);
                    Map<String,File> submap_map = new HashMap<>();
                    for (File map_f:db_f.listFiles()) {
                        if(map_f.getName().endsWith("_map")) {
                            MapBuilder mb = db.newMap(map_f.getName().split("_map")[0]);
                            for (File file:map_f.listFiles()) {

                            }
                            mb.threshold()
                        }else if(map_f.getName().endsWith("_submap")) {
                            submap_map.put(map_f.getName().split(""))
                        }
                    }
                }
            }
        }
    }
}

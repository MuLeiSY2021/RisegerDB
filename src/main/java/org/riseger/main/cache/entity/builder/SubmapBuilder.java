package org.riseger.main.cache.entity.builder;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.entity.component.map.Layer_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;

public class SubmapBuilder {
    public static MapDB_c build(String name, Layer_c layer, Database_c database) {
        return new MapDB_c(null,
                layer.getParent().getParent().getNodeSize(),
                layer.getParent().getParent().getThreshold(),
                name,
                database);
    }
}

package org.riseger.main.cache.entity.builder;

import org.riseger.main.cache.entity.component.map.Layer_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;

public class SubmapBuilder {
    public static MapDB_c build(String name, Layer_c layer) {
        return new MapDB_c(null,
                layer.getParent().getParent().getNodeSize(),
                layer.getParent().getParent().getThreshold(),
                name,
                null);
    }
}

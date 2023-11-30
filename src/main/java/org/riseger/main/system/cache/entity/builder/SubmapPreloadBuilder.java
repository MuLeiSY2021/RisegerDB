package org.riseger.main.system.cache.entity.builder;

import lombok.Data;
import org.riseger.main.system.cache.entity.component.Database_c;
import org.riseger.main.system.cache.entity.component.Layer_c;
import org.riseger.main.system.cache.entity.component.MapDB_c;
import org.riseger.main.system.cache.manager.ElementManager;

@Data
public class SubmapPreloadBuilder implements MapBuilder {
    String name;

    Layer_c layer;

    Database_c database;

    ElementManager em;


    public MapDB_c build() {
        return new MapDB_c(name, layer, database, em);
    }
}

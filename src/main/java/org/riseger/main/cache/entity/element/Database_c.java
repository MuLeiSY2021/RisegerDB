package org.riseger.main.cache.entity.element;

import lombok.Data;
import org.riseger.main.cache.entity.manager.DBConfigManager;
import org.riseger.main.cache.entity.manager.MapDBManager;
import org.riseger.main.cache.entity.manager.ModelManager;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Model;

@Data
public class Database_c {
    String name;

    MapDBManager maps = new MapDBManager();

    DBConfigManager configs = new DBConfigManager(this);

    ModelManager models = new ModelManager();

    public Database_c(String name) {
        this.name = name;
    }

    public void addConfig(Config config,String value) {
        configs.addConfig(config,value);
    }

    public void addModel(Model model) {

    }

    public void addMap(MapDB map) {
    }
}

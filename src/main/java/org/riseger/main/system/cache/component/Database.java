package org.riseger.main.system.cache.component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.riseger.main.constant.Status;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.MapManager;
import org.riseger.main.system.cache.manager.ModelManager;
import org.riseger.protocol.struct.entity.GeoMap_p;
import org.riseger.protocol.struct.entity.Model_p;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Database extends HolisticStorageEntity {
    private String name;

    private Status status = Status.LOADING;

    private MapManager mapManager = new MapManager(this);

    private ConfigManager configManager = new ConfigManager();

    private ModelManager modelManager = new ModelManager(this);

    public Database(String name) {
        this.name = name;
    }

    public void addConfig(Config config, String value) {
        configManager.addConfig(config, value);
    }

    public void addModel(Model_p model) {
        modelManager.addModel(model);
    }

    public void addMap(GeoMap_p map) {
        mapManager.addMap(Integer.parseInt(map.getConfig("nodeSize")),
                Double.parseDouble(map.getConfig("threshold")),
                map);
    }

    public void active() {
        this.status = Status.ACTIVE;
    }

    public List<org.riseger.main.system.cache.component.GeoMap> getMapDBs() {
        return mapManager.getMaps();
    }

    public void initMap(org.riseger.main.system.cache.component.GeoMap map) {
        mapManager.initMap(map);
    }

    public org.riseger.main.system.cache.component.GeoMap getMap(String map) {
        return this.mapManager.getMap(map);
    }

    public List<Model> getModels() {
        return this.modelManager.getModels();
    }

    public double getThreshold() {
        return this.getConfigManager().getThreshold();
    }
}

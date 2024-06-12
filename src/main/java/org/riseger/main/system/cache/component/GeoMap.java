package org.riseger.main.system.cache.component;

import lombok.Getter;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.HolisticStorageEntity_i;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.ElementManager;
import org.riseger.main.system.cache.manager.LayerManager;
import org.riseger.protocol.struct.entity.Element_p;

import java.io.File;
import java.util.List;

@Getter
public class GeoMap extends HolisticStorageEntity {
    private final String name;

    private final HolisticStorageEntity_i entity = new HolisticStorageEntity();

    private final ConfigManager configManager = new ConfigManager();

    //TODO:Index managers should exist
    //private IndexManager indexManager = new IndexManager();

    private transient final LayerManager layerManager;
    private transient final Database database;


    public GeoMap(int nodeSize, double threshold, String name, Database database) {
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        configManager.addConfig(new Config("node_size", String.valueOf(nodeSize)), "node_size");
        configManager.addConfig(new Config("threshold", String.valueOf(threshold)), "threshold");
    }

    public GeoMap(ConfigManager configManager, String name, Database database) {
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        this.configManager.addAll(configManager);
    }

    public GeoMap(ConfigManager configManager, String name, Database database, ElementManager em) {
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        this.configManager.addAll(configManager);
    }

    public GeoMap(String name, Layer layer, Database database) {
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        configManager.addConfig(new Config("node_size", String.valueOf(layer.getParent().getParent().getNodeSize())), "node_size");
        configManager.addConfig(new Config("threshold", String.valueOf(layer.getParent().getParent().getThreshold())), "threshold");
    }

    public void preloadElement(Element_p e) {
        layerManager.addElement(e);
    }

    public void initAllMd(List<File> mdLayer) {
        for (File md_ : mdLayer) {
            layerManager.initMdLayer(md_);
        }
    }

    public int getNodeSize() {
        return configManager.getInt("node_size");
    }

    public double getThreshold() {
        return configManager.getDouble("threshold");
    }

    public Layer getElementLayer(String s) {
        return this.layerManager.getElement(s);
    }

}

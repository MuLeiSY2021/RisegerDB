package org.riseger.main.system.cache.component;

import lombok.Getter;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.HolisticStorageEntity_i;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.ElementManager;
import org.riseger.main.system.cache.manager.LayerManager;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.util.List;
import java.util.Map;

@Getter
public class Map_c extends MBRectangle_c implements HolisticStorageEntity_i {
    private final String name;

    private final HolisticStorageEntity_i entity = new HolisticStorageEntity();

    private final ConfigManager configManager = new ConfigManager();

    private transient final LayerManager layerManager;
    private transient final Database_c database;
    private transient ElementManager smp_parent;

    public Map_c(int nodeSize, double threshold, String name, Database_c database) {
        super(threshold);
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        configManager.addConfig(new Config_c("node_size", String.valueOf(nodeSize)), "node_size");
        configManager.addConfig(new Config_c("threshold", String.valueOf(threshold)), "threshold");
    }

    public Map_c(Map<String, Config_c> configManager, String name, Database_c database) {
        super(configManager.get("threshold").getDoubleValue());
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        this.configManager.addAll(configManager);
    }

    public Map_c(Map<String, Config_c> configManager, String name, Database_c database, ElementManager em) {
        super(configManager.get("threshold").getDoubleValue());
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        this.configManager.addAll(configManager);
        this.smp_parent = em;
    }

    public Map_c(String name, Layer_c layer, Database_c database, ElementManager em) {
        super(layer.getParent().getParent().getThreshold());
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        configManager.addConfig(new Config_c("node_size", String.valueOf(layer.getParent().getParent().getNodeSize())), "node_size");
        configManager.addConfig(new Config_c("threshold", String.valueOf(layer.getParent().getParent().getThreshold())), "threshold");
        this.smp_parent = em;
    }

    public void preloadElement(Element e) {
        layerManager.addElement(e);
    }

    public void preloadSubmap(Submap map) {
        layerManager.preloadSubmap(map);
    }

    public void preloadSubmap(Submap submapChild, int index) {
        layerManager.preloadSubmap(submapChild, index);
    }

    public void updateBoundary(Rectangle mbr) {
        super.expand(mbr);
        if (smp_parent != null) {
            smp_parent.updateIndex(this, mbr);
        }

    }

    public int getNodeSize() {
        return configManager.getInt("node_size");
    }

    public double getThreshold() {
        return configManager.geDouble("threshold");
    }

    public void initAllSmp(List<File> submaps) {
        for (File submap_ : submaps) {
            layerManager.initSmpLayer(submap_);
        }
    }

    public void initAllMd(List<File> mdLayer) {
        for (File md_ : mdLayer) {
            layerManager.initMdLayer(md_);
        }
    }

    public Layer_c getSubmapLayer(String s) {
        return this.layerManager.getSubmap(s);
    }

    public Layer_c getElementLayer(String s) {
        return this.layerManager.getElement(s);
    }

    @Override
    public void changeEntity() {
        entity.changeEntity();
    }

    @Override
    public void resetChanged() {
        entity.resetChanged();
    }

    @Override
    public boolean isChanged() {
        return entity.isChanged();
    }
}

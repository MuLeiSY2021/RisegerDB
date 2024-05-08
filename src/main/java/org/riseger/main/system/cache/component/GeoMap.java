package org.riseger.main.system.cache.component;

import lombok.Getter;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.HolisticStorageEntity_i;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.ElementManager;
import org.riseger.main.system.cache.manager.LayerManager;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.protocol.struct.entity.Submap;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Getter
public class GeoMap extends GeoRectangle implements HolisticStorageEntity_i {
    private final String name;

    private final HolisticStorageEntity_i entity = new HolisticStorageEntity();

    private final ConfigManager configManager = new ConfigManager();

    private transient final LayerManager layerManager;
    private transient final Database database;
    private transient ElementManager smp_parent;

    public GeoMap(int nodeSize, double threshold, String name, Database database) {
        super(threshold);
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        configManager.addConfig(new Config("node_size", String.valueOf(nodeSize)), "node_size");
        configManager.addConfig(new Config("threshold", String.valueOf(threshold)), "threshold");
    }

    public GeoMap(ConfigManager configManager, String name, Database database) {
        super(configManager.getThreshold());
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        this.configManager.addAll(configManager);
    }

    public GeoMap(ConfigManager configManager, String name, Database database, ElementManager em) {
        super(configManager.getThreshold());
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        this.configManager.addAll(configManager);
        this.smp_parent = em;
    }

    public GeoMap(String name, Layer layer, Database database, ElementManager em) {
        super(layer.getParent().getParent().getThreshold());
        this.name = name;
        this.layerManager = new LayerManager(this);
        this.database = database;
        configManager.addConfig(new Config("node_size", String.valueOf(layer.getParent().getParent().getNodeSize())), "node_size");
        configManager.addConfig(new Config("threshold", String.valueOf(layer.getParent().getParent().getThreshold())), "threshold");
        this.smp_parent = em;
    }

    public void preloadElement(Element_p e) {
        layerManager.addElement(e);
    }

    public void preloadSubmap(Submap map) {
        layerManager.preloadSubmap(map);
    }

    public void preloadSubmap(Submap submapChild, int index) {
        layerManager.preloadSubmap(submapChild, index);
    }

    public void initAllSmp(List<File> submaps) throws IOException {
        for (File submap_ : submaps) {
            layerManager.initSmpLayer(submap_);
        }
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

    public Layer getSubmapLayer(String s) {
        return this.layerManager.getSubmap(s);
    }

    public Layer getElementLayer(String s) {
        return this.layerManager.getElement(s);
    }

    public void expandBoundary(Rectangle mbr) {
        super.expand(mbr);
        if (smp_parent != null) {
            smp_parent.updateIndex(this, mbr);
        }
    }

    public void setBoundary(Rectangle mbr) throws CloneNotSupportedException {
        super.resetIndex(mbr);
        if (smp_parent != null) {
            smp_parent.setIndex(this, mbr);
        }
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

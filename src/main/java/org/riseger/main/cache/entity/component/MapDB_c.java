package org.riseger.main.cache.entity.component;

import lombok.Getter;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.cache.manager.LayerManager;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

@Getter
public class MapDB_c extends MBRectangle_c {
    private final String name;

    private final Map<String, Config> configs = new HashMap<>();

    private transient final LayerManager layers;
    private transient final Database_c database;
    private transient ElementManager smp_parent;

    public MapDB_c(ConcurrentMap<String, String> map, int nodeSize, double threshold, String name, Database_c database) {
        super(map, threshold);
        this.name = name;
        this.layers = new LayerManager(this);
        this.database = database;
        configs.put("node_size", new Config("node_size", String.valueOf(nodeSize)));
        configs.put("threshold", new Config("threshold", String.valueOf(threshold)));
    }

    public MapDB_c(ConcurrentMap<String, String> map, Map<String, Config> configs, String name, Database_c database) {
        super(map, configs.get("threshold").getDoubleValue());
        this.name = name;
        this.layers = new LayerManager(this);
        this.database = database;
        this.configs.putAll(configs);
    }

    public MapDB_c(ConcurrentMap<String, String> map, Map<String, Config> configs, String name, Database_c database, ElementManager em) {
        super(map, configs.get("threshold").getDoubleValue());
        this.name = name;
        this.layers = new LayerManager(this);
        this.database = database;
        this.configs.putAll(configs);
        this.smp_parent = em;
    }

    public MapDB_c(String name, Layer_c layer, Database_c database, ElementManager em) {
        super(null, layer.getParent().getParent().getThreshold());
        this.name = name;
        this.layers = new LayerManager(this);
        this.database = database;
        configs.put("node_size", new Config("node_size", String.valueOf(layer.getParent().getParent().getNodeSize())));
        configs.put("threshold", new Config("threshold", String.valueOf(layer.getParent().getParent().getThreshold())));
        this.smp_parent = em;
    }

    public void preloadElement(Element e) {
        layers.addElement(e);
    }

    public void preloadSubmap(Submap map) {
        layers.preloadSubmap(map);
    }

    public void preloadSubmap(Submap submapChild, int index) {
        layers.preloadSubmap(submapChild, index);
    }

    public void updateBoundary(MBRectangle_c mbr) {
        if (smp_parent != null) {
            smp_parent.remove(this);
        }
        super.expand(mbr);
        if (smp_parent != null) {
            smp_parent.addElement(this);
        }
    }

    public int getNodeSize() {
        return configs.get("node_size").getIntValue();
    }

    public double getThreshold() {
        return configs.get("threshold").getDoubleValue();
    }

    public void initAllSmp(List<File> submaps) {
        for (File submap_ : submaps) {
            layers.initSmpLayer(submap_);
        }
    }

    public void initAllMd(List<File> mdLayer) {
        for (File md_ : mdLayer) {
            layers.initMdLayer(md_);
        }
    }
}

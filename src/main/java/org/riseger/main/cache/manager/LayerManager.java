package org.riseger.main.cache.manager;

import lombok.Data;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class LayerManager {
    private final Map<String, Layer_c> layerMap = new HashMap<>();

    private final MapDB_c parent;

    public LayerManager(MapDB_c parent) {
        this.parent = parent;
    }

    public void preloadSubmap(Submap submap) {
        preloadSubmap(submap, 0);
    }

    public void preloadSubmap(Submap submap, int index) {
        Layer_c layer;
        String name = getLName(submap, index);
        if (layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name, this, parent.getNodeSize(), parent.getThreshold());
            layerMap.put(name, layer);
        }
        layer.preloadSubmap(submap, index + 1);
    }

    public String getLName(Submap submap, int index) {
        return Constant.SUBMAP_PREFIX + "_" + submap.getScopePath().split("\\.")[index];
    }

    public String getLName(Element e) {
        return Constant.MODEL_PREFIX + "_" + e.getModelName();
    }

    public String getElementName(String name) {
        return Constant.MODEL_PREFIX + "_" + name;
    }

    public String getSubmapName(String name) {
        return Constant.SUBMAP_PREFIX + "_" + name;
    }

    public void addElement(Element e) {
        Layer_c layer;
        String name = getLName(e);
        if (layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name, this, parent.getNodeSize(), parent.getThreshold());
            layerMap.put(name, layer);
        }
        if (layer == null) {
            throw new RuntimeException("Layer " + name + " not found");
        }
        layer.preloadElement(e);
    }

    public void expand(Rectangle eC) {
        parent.updateBoundary(eC);
    }

    public Layer_c[] toList() {
        return this.layerMap.values().toArray(new Layer_c[0]);
    }

    public Layer_c getSubmap(String s) {
        return this.layerMap.get(getSubmapName(s));
    }

    public Layer_c getElement(String s) {
        return this.layerMap.get(getElementName(s));
    }

    public void initSmpLayer(File layer_) {
        String name = Utils.getNameFromFile(layer_);
        Layer_c layer = new Layer_c(name, this, parent.getNodeSize(), parent.getThreshold());
        this.layerMap.put(name, layer);
        for (File smp : Objects.requireNonNull(layer_.listFiles())) {
            layer.initSubMap(smp);
        }
    }

    public void initMdLayer(File layer_) {
        String name = Utils.getNameFromFile(layer_);
        Layer_c layer = new Layer_c(name, this, layer_);
        layerMap.put(name, layer);
    }
}

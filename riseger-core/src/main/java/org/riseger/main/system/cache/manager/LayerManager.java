package org.riseger.main.system.cache.manager;

import lombok.Data;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.cache.component.Layer;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.protocol.struct.entity.Submap;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class LayerManager extends HolisticStorageEntity {
    private final Map<String, Layer> layerMap = new ConcurrentHashMap<>();

    private final GeoMap parent;

    public LayerManager(GeoMap parent) {
        this.parent = parent;
    }

    public void preloadSubmap(Submap submap) {
        preloadSubmap(submap, 0);
    }

    public void preloadSubmap(Submap submap, int index) {
        Layer layer;
        String name = getLName(submap, index);
        if (layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer(name, this, parent.getNodeSize(), parent.getThreshold());
            layerMap.put(name, layer);
        }
        layer.preloadSubmap(submap, index + 1);
    }

    public String getLName(Submap submap, int index) {
        return Constant.SUBMAP_PREFIX + "_" + submap.getScopePath().split("\\.")[index];
    }

    public String getLName(Element_p e) {
        return Constant.MODEL_PREFIX + "_" + e.getModelName();
    }

    public String getElementName(String name) {
        return Constant.MODEL_PREFIX + "_" + name;
    }

    public String getSubmapName(String name) {
        return Constant.SUBMAP_PREFIX + "_" + name;
    }

    public void addElement(Element_p e) {
        Layer layer;
        String name = getLName(e);
        if (layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer(name, this, parent.getNodeSize(), parent.getThreshold());
            layerMap.put(name, layer);
        }
        if (layer == null) {
            throw new RuntimeException("Layer " + name + " Not_f found");
        }
        layer.preloadElement(e);
    }

    public void expand(Rectangle eC) {
        parent.expandBoundary(eC);
    }

    public void adjust(Rectangle eC) throws CloneNotSupportedException {
        parent.setBoundary(eC);
    }

    public Layer[] toList() {
        return this.layerMap.values().toArray(new Layer[0]);
    }

    public Layer getSubmap(String s) {
        return this.layerMap.get(getSubmapName(s));
    }

    public Layer getElement(String s) {
        return this.layerMap.get(getElementName(s));
    }

    public void initSmpLayer(File layer_) throws IOException {
        String name = Utils.getNameFromFile(layer_);
        Layer layer = new Layer(name, this, parent.getNodeSize(), parent.getThreshold());
        this.layerMap.put(name, layer);
        for (File smp : Objects.requireNonNull(layer_.listFiles())) {
            layer.initSubMap(smp);
        }
    }

    public void initMdLayer(File layer_) {
        String name = Utils.getNameFromFile(layer_);
        Layer layer = new Layer(name, this, layer_);
        layerMap.put(name, layer);
    }
}

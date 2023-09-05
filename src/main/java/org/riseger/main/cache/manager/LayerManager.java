package org.riseger.main.cache.manager;

import lombok.Data;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.builder.SubmapInitBuilder;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String name = getLName(submap);
        if (layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name, this, parent.getNodeSize(), parent.getThreshold());
            layerMap.put(name, layer);
        }
        layer.preloadSubmap(submap, index);
    }

    public String getLName(Submap submap) {
        return Constant.SUBMAP_PREFIX + "_" + submap.getScopePath();
    }

    public String getLName(Element e) {
        return Constant.MODEL_PREFIX + "_" + e.getModelName();
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

    public void expand(MBRectangle_c eC) {
        parent.updateBoundary(eC);
    }

    public Layer_c[] toList() {
        return this.layerMap.values().toArray(new Layer_c[0]);
    }

    public void addAllSubmap(List<SubmapInitBuilder> submaps) throws Exception {
        for (SubmapInitBuilder submap : submaps) {
            Layer_c layer;
            String name = submap.getName();
            if (layerMap.containsKey(name)) {
                layer = layerMap.get(name);
            } else {
                layer = new Layer_c(name, this, parent.getNodeSize(), parent.getThreshold());
                layerMap.put(name, layer);
            }
            submap.setEm(layer.getElementManager());
            layer.addSubmap(submap.build());
        }
    }

    public Layer_c get(String s) {
        return this.layerMap.get(s);
    }

    public void deserialize(File layer_, String name) throws Exception {
        Layer_c layer = new Layer_c(name, this, layer_);
        layerMap.put(name, layer);
    }
}

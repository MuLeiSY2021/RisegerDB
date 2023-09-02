package org.riseger.main.cache.manager;

import lombok.Data;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.component.map.Layer_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;
import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

import java.util.HashMap;
import java.util.Map;

@Data
public class LayerManager {
    private final Map<String, Layer_c> layerMap = new HashMap<>();

    private final MapDB_c parent;

    public LayerManager(MapDB_c parent) {
        this.parent = parent;
    }

    public void addSubmap(Submap submap) {
        addSubmap(submap,0);
    }

    public void addSubmap(Submap submap, int index) {
        Layer_c layer;
        String[] paths = submap.getScopePath().split("\\.");
        String name = getLName(submap);
        if(layerMap.containsKey(paths[index])) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name,this, parent.getNodeSize(), parent.getThreshold());
        }
        layer.addSubmap(submap,index + 1);
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
        if(layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name,this, parent.getNodeSize(), parent.getThreshold());
            layerMap.put(name, layer);
        }
        if(layer == null) {
            throw new RuntimeException("Layer " + name  + " not found");
        }
        layer.addElement(e);
    }

    public void expand(MBRectangle_c eC) {
        parent.updateBoundary(eC);
    }

    public Layer_c[] toList() {
        return this.layerMap.values().toArray(new Layer_c[0]);
    }
}

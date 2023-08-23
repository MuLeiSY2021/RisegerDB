package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.element.Layer_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

import java.util.HashMap;
import java.util.Map;

public class LayerManager {
    private Map<String, Layer_c> layerMap = new HashMap<>();

    public void addElement(Element e) {
        Layer_c layer = null;
        String name = getLName(e);
        if(layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name);
            layerMap.put(name, layer);
        }
        if(layer == null) {
            throw new RuntimeException("Layer " + name  + " not found");
        }
        layer.addElement(e);
    }

    public void addSubmap(Submap submap) {
        addSubmap(submap,0);
    }

    public void addSubmap(Submap submap, int index) {
        Layer_c layer = null;
        String[] paths = submap.getScopePath().split("/");
        if(layerMap.containsKey(paths[index])) {
            layer = layerMap.get((getLName(submap)));
        } else {

            layer = new Layer_c(getLName(submap));
        }
        layer.addSubmap(submap,index + 1);
    }

    public String getLName(Submap submap) {
        return "sb_" + submap.getScopePath().split("/")[0];
    }

    public String getLName(Element e) {
        return "md_" + e.getModelName();
    }


}

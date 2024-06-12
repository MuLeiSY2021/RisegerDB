package org.riseger.main.system.cache.manager;

import lombok.Data;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.cache.component.Layer;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.utils.Utils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class LayerManager extends HolisticStorageEntity {
    private final Map<String, Layer> layerMap = new ConcurrentHashMap<>();

    private final GeoMap parent;

    public LayerManager(GeoMap parent) {
        this.parent = parent;
    }

    public String getLName(Element_p e) {
        return Constant.MODEL_PREFIX + "_" + e.getModelName();
    }

    public String getElementName(String name) {
        return Constant.MODEL_PREFIX + "_" + name;
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

    public Layer[] toList() {
        return this.layerMap.values().toArray(new Layer[0]);
    }

    public Layer getElement(String s) {
        return this.layerMap.get(getElementName(s));
    }

    public void initMdLayer(File layer_) {
        String name = Utils.getNameFromFile(layer_);
        Layer layer = new Layer(name, this, layer_);
        layerMap.put(name, layer);
    }
}

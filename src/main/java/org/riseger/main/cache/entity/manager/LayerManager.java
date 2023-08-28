package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.entity.component.map.Layer_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;
import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

import java.util.HashMap;
import java.util.Map;

public class LayerManager {
    private final Map<String, Layer_c> layerMap = new HashMap<>();

    private final MapDB_c mapDBC;

    public LayerManager(MapDB_c mapDBC) {
        this.mapDBC = mapDBC;
    }

    public void addSubmap(Submap submap) {
        addSubmap(submap,0);
    }

    public void addSubmap(Submap submap, int index) {
        Layer_c layer;
        String[] paths = submap.getScopePath().split("/");
        String name = getLName(submap);
        if(layerMap.containsKey(paths[index])) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name,this,mapDBC.getNodeSize(),mapDBC.getThreshold());
        }
        layer.addSubmap(submap,index + 1);
    }

    public String getLName(Submap submap) {
        return "sb_" + submap.getScopePath().split("/")[0];
    }

    public String getLName(Element e) {
        return "md_" + e.getModelName();
    }

    public void addElement(Element e, Database_c db) {
        Layer_c layer;
        String name = getLName(e);
        if(layerMap.containsKey(name)) {
            layer = layerMap.get(name);
        } else {
            layer = new Layer_c(name,this,mapDBC.getNodeSize(),mapDBC.getThreshold());
            layerMap.put(name, layer);
        }
        if(layer == null) {
            throw new RuntimeException("Layer " + name  + " not found");
        }
        layer.addElement(e,db,mapDBC);
    }

    public MapDB_c getMap() {
        return mapDBC;
    }

    public void expand(MBRectangle_c eC) {
        mapDBC.updateBoundary(eC);
    }
}

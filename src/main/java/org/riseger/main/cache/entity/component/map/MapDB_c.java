package org.riseger.main.cache.entity.component.map;

import lombok.Getter;
import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.cache.manager.LayerManager;
import org.riseger.main.cache.manager.MapDBManager;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.MapDB;
import org.riseger.protoctl.struct.entity.Submap;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MapDB_c extends MBRectangle_c {
    private final String name;

    private final Map<String,Config> configs = new HashMap<>();

    private final LayerManager layers;

    //如果是Submap就是null
    private final MapDBManager parent;

    private ElementManager elementManager;

    public MapDB_c(Map<String, String> map,int nodeSize, double threshold, String name,MapDBManager parent) {
        super(map, threshold);
        this.name = name;
        this.layers = new LayerManager(this);
        this.parent = parent;
        configs.put("node_size",new Config("node_size",String.valueOf(nodeSize)));
        configs.put("threshold",new Config("threshold",String.valueOf(threshold)));
    }

    public static MapDB_c mapBuilder(MapDB map,int nodeSize, double threshold,MapDBManager parent) {
        MapDB_c mapDB = new MapDB_c(null, nodeSize, threshold, map.getName(),parent);
        for (Submap submap:map.getSubmaps()) {
            mapDB.addSubmap(submap);
        }

        for (Element e:map.getElements()) {
            mapDB.addElement(e);
        }
        return mapDB;
    }

    public void addElement(Element e) {
        layers.addElement(e);
    }

    public void addSubmap(Submap map) {
        layers.addSubmap(map);
    }

    public void addSubmap(Submap submapChild, int index) {
        layers.addSubmap(submapChild,index);
    }

    public void updateBoundary(MBRectangle_c mbr) {
        if(elementManager!= null) {
            elementManager.remove(this);
        }
        super.expand(mbr);
        if(elementManager!= null) {
            elementManager.addElement(this);
        }
    }

    public int getNodeSize() {
        return configs.get("node_size").getIntValue();
    }

    public double getThreshold() {
        return configs.get("threshold").getDoubleValue();
    }

    public String toFIleName() {
        return name + "." + super.toFileString();
    }
}

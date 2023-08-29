package org.riseger.main.cache.entity.component.map;

import org.riseger.main.cache.entity.component.db.Database_c;
import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.cache.manager.LayerManager;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

public class Layer_c {
    private String name;

    private final LayerManager layerManager;

    private final ElementManager elementManager;

    public Layer_c(String name, LayerManager layerManager, int nodeSize, double threshold) {
        this.name = name;
        this.layerManager = layerManager;
        this.elementManager = ElementManager.buildRStartElementManager(nodeSize,threshold,this);
    }
    

    public void addSubmap(Submap submap, int index) {
        for (Element e:submap.getElements()) {
            addElement(e);
        }
        for (Submap submap_child:submap.getSubmaps()) {
            layerManager.addSubmap(submap_child,index);
        }
    }

    public void addElement(Element e) {
        elementManager.addElement(e);
    }


    public String getName() {
        return null;
    }

    public void addElement(Element e, Database_c db, MapDB_c mapDBC) {

    }

    public LayerManager getLayerManager() {
        return layerManager;
    }

    public void expand(MBRectangle_c eC) {
        layerManager.expand(eC);
    }
}

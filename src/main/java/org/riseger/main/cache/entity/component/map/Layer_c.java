package org.riseger.main.cache.entity.component.map;

import org.riseger.main.cache.entity.manager.ElementManager;
import org.riseger.main.cache.entity.manager.LayerManager;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

public class Layer_c {
    private String name;

    private LayerManager submapManager;

    private ElementManager elementManager;

    public Layer_c(String name) {
        this.name = name;
    }

    public void addElement(Element e) {

    }

    public Layer_c addSubmap() {
        return null;
    }

    public void addSubmap(Submap submap, int index) {
        for (Element e:submap.getElements()) {
            addElement(e);
        }
        for (Submap submap_child:submap.getSubmaps()) {
            submapManager.addSubmap(submap_child,index);
        }
    }

    public String getName() {
        return null;
    }
}

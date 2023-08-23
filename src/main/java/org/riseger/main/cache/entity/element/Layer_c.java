package org.riseger.main.cache.entity.element;

import org.riseger.main.cache.entity.mamager.ElementManager;
import org.riseger.main.cache.entity.mamager.LayerManager;
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

    public Layer_c addSubap() {
        return null;
    }

    public Submap_c addSubap(Submap submap,int index) {
        for (Element e:submap.getElements()) {
            addElement(e);
        }
        for (Submap submap_child:submap.getSubmaps()) {
            submapManager.addSubmap(submap_child,index);
        }
    }

    public String getName() {
        return this.;
    }
}

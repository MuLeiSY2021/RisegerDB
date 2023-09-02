package org.riseger.main.cache.entity.component.map;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.builder.SubmapBuilder;
import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.cache.manager.LayerManager;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

@Getter
@Setter
public class Layer_c {
    private String name;

    private final LayerManager parent;

    private final ElementManager elementManager;

    public Layer_c(String name, LayerManager parent, int nodeSize, double threshold) {
        this.name = name;
        this.parent = parent;
        this.elementManager = ElementManager.buildRStartElementManager(nodeSize,threshold,this);
    }
    

    public void addSubmap(Submap submap, int index) {
        MapDB_c map = SubmapBuilder.build(
                submap.getName(),
                this,parent.getParent().getDatabase());
        elementManager.addElement(map);
        for (Element e:submap.getElements()) {
            map.addElement(e);
        }
        for (Submap submap_child:submap.getSubmaps()) {
            map.addSubmap(submap_child,index);
        }
    }

    public void addElement(Element e) {
        elementManager.addElement(e);
    }
    
    public LayerManager getParent() {
        return parent;
    }

    public void expand(MBRectangle_c eC) {
        parent.expand(eC);
    }

    public boolean isSubMap() {
        return name.startsWith(Constant.SUBMAP_PREFIX);
    }
}

package org.riseger.main.cache.entity.component;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.builder.SubmapPreloadBuilder;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.cache.manager.LayerManager;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;

import java.io.File;

@Getter
@Setter
public class Layer_c {
    private final LayerManager parent;
    private final ElementManager elementManager;
    private String name;

    public Layer_c(String name, LayerManager parent, int nodeSize, double threshold) {
        this.name = name;
        this.parent = parent;
        this.elementManager = ElementManager.buildRStartElementManager(nodeSize, threshold, this);
    }

    public Layer_c(String name, LayerManager parent, File layer_) throws Exception {
        this.name = name;
        this.parent = parent;
        this.elementManager = ElementManager.deserializeRStartElementManager(this, layer_);
    }

    public void preloadSubmap(Submap submap, int index) {
        SubmapPreloadBuilder submapPreloadBuilder = new SubmapPreloadBuilder();
        submapPreloadBuilder.setLayer(this);
        submapPreloadBuilder.setEm(elementManager);
        submapPreloadBuilder.setName(submap.getName());
        submapPreloadBuilder.setDatabase(parent.getParent().getDatabase());
        MapDB_c map = submapPreloadBuilder.build();
        for (Element e : submap.getElements()) {
            map.preloadElement(e);
        }
        for (Submap submap_child : submap.getSubmaps()) {
            map.preloadSubmap(submap_child, index);
        }
        elementManager.addElement(map);

    }

    public void preloadElement(Element e) {
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

    public void addSubmap(MapDB_c submap) {
        elementManager.addElement(submap);
    }
}

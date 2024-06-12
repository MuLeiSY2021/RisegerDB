package org.riseger.main.system.cache.component;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.manager.ElementManager;
import org.riseger.main.system.cache.manager.LayerManager;
import org.riseger.protocol.struct.entity.Element_p;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class Layer extends HolisticStorageEntity {
    @Getter
    private final LayerManager parent;

    private final ElementManager elementManager;

    private String name;

    public Layer(String name, LayerManager parent, int nodeSize, double threshold) {
        this.name = name;
        this.parent = parent;
        this.elementManager = ElementManager.buildRStartElementManager(nodeSize, threshold, this, Element.class);
    }

    public Layer(String name, LayerManager parent, File layer_) {
        this.name = name;
        this.parent = parent;
        this.elementManager = ElementManager.deserializeRStartElementManager(this, layer_);
    }

    public void preloadElement(Element_p e) {
        elementManager.addElement(e);
    }

    public List<GeoRectangle> getElements(GeoRectangle scope) {
        return elementManager.getElements(scope);
    }
}

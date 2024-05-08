package org.riseger.main.system.cache.component;

import lombok.Getter;
import lombok.Setter;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.HolisticStorageEntity;
import org.riseger.main.system.cache.builder.SubmapInitBuilder;
import org.riseger.main.system.cache.manager.ConfigManager;
import org.riseger.main.system.cache.manager.ElementManager;
import org.riseger.main.system.cache.manager.LayerManager;
import org.riseger.protocol.serializer.JsonSerializer;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.protocol.struct.entity.Submap;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
        if (isSubMap()) {
            this.elementManager = ElementManager.buildRStartElementManager(nodeSize, threshold, this, GeoMap.class);
        } else {
            this.elementManager = ElementManager.buildRStartElementManager(nodeSize, threshold, this, Element.class);
        }
    }

    public Layer(String name, LayerManager parent, File layer_) {
        this.name = name;
        this.parent = parent;
        this.elementManager = ElementManager.deserializeRStartElementManager(this, layer_);
        this.parent.expand(this.elementManager.getRtreeKeyIndex().getSquareRect());
    }

    private boolean isModel() {
        return name.startsWith(Constant.MODEL_PREFIX);
    }

    public void preloadSubmap(Submap submap, int index) {
        GeoMap map = new GeoMap(submap.getName(),
                this,
                parent.getParent().getDatabase(),
                elementManager);
        for (Element_p e : submap.getElements()) {
            map.preloadElement(e);
        }
        for (Submap submap_child : submap.getSubmaps()) {
            map.preloadSubmap(submap_child, index);
        }
        elementManager.addElement(map);

    }

    public void preloadElement(Element_p e) {
        elementManager.addElement(e);
    }

    public void expand(Rectangle eC) {
        parent.expand(eC);
    }

    public void adjust(Rectangle eC) throws CloneNotSupportedException {
        parent.adjust(eC);
    }

    public boolean isSubMap() {
        return name.startsWith(Constant.SUBMAP_PREFIX);
    }

    public void initSubMap(File map_) throws IOException {
        SubmapInitBuilder submapInitBuilder = new SubmapInitBuilder();
        submapInitBuilder.setDatabase(parent.getParent().getDatabase());
        submapInitBuilder.setName(Utils.getNameFromFile(map_));
        submapInitBuilder.setEm(elementManager);
        for (File smp_ : Objects.requireNonNull(map_.listFiles())) {
            if (smp_.getName().endsWith(Constant.LAYER_PREFIX)) {
                submapInitBuilder.addLayer(smp_);
            } else if (smp_.getName().equals(Constant.CONFIG_FILE_NAME
                    + Constant.DOT_PREFIX
                    + Constant.JSON_PREFIX)) {
                ConfigManager configs = JsonSerializer.deserialize(Utils.getBytes(smp_), ConfigManager.class);
                submapInitBuilder.setConfigs(configs);
            }
        }
        elementManager.addElement(submapInitBuilder.build());
    }

    public List<GeoRectangle> getElements(GeoRectangle scope) {
        return elementManager.getElements(scope);
    }
}

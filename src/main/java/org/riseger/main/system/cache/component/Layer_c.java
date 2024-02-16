package org.riseger.main.system.cache.component;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.riseger.main.constant.Constant;
import org.riseger.main.system.cache.Entity;
import org.riseger.main.system.cache.builder.SubmapInitBuilder;
import org.riseger.main.system.cache.manager.ElementManager;
import org.riseger.main.system.cache.manager.LayerManager;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
public class Layer_c extends Entity {
    @Getter
    private final LayerManager parent;

    private final ElementManager elementManager;

    private String name;

    public Layer_c(String name, LayerManager parent, int nodeSize, double threshold) {
        this.name = name;
        this.parent = parent;
        if (isSubMap()) {
            this.elementManager = ElementManager.buildRStartElementManager(nodeSize, threshold, this, Map_c.class);
        } else {
            this.elementManager = ElementManager.buildRStartElementManager(nodeSize, threshold, this, Element_c.class);
        }
    }

    public Layer_c(String name, LayerManager parent, File layer_) {
        this.name = name;
        this.parent = parent;
        this.elementManager = ElementManager.deserializeRStartElementManager(this, layer_);
        this.parent.expand(this.elementManager.getRtreeKeyIndex().getSquareRect());
    }

    private boolean isModel() {
        return name.startsWith(Constant.MODEL_PREFIX);
    }

    public void preloadSubmap(Submap submap, int index) {
        Map_c map = new Map_c(submap.getName(),
                this,
                parent.getParent().getDatabase(),
                elementManager);
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

    public void expand(Rectangle eC) {
        parent.expand(eC);
    }

    public boolean isSubMap() {
        return name.startsWith(Constant.SUBMAP_PREFIX);
    }

    public void initSubMap(File map_) {
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
                Map<String, Config_c> configs = (Map<String, Config_c>) JsonSerializer.deserialize(Utils.getText(smp_),
                        TypeToken.getParameterized(HashMap.class, String.class, Config_c.class));
                submapInitBuilder.setConfigs(configs);
            }
        }
        elementManager.addElement(submapInitBuilder.build());
    }

    public List<MBRectangle_c> getElements(MBRectangle_c scope) {
        return elementManager.getElements(scope);
    }
}

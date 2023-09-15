package org.riseger.main.cache.entity.component;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.riseger.main.Constant;
import org.riseger.main.cache.entity.builder.SubmapInitBuilder;
import org.riseger.main.cache.entity.builder.SubmapPreloadBuilder;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.main.cache.manager.LayerManager;
import org.riseger.protoctl.serializer.JsonSerializer;
import org.riseger.protoctl.struct.config.Config;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.protoctl.struct.entity.Submap;
import org.riseger.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public Layer_c(String name, LayerManager parent, File layer_) {
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

    public void initSubMap(File map_) {
        SubmapInitBuilder submapInitBuilder = new SubmapInitBuilder();
        submapInitBuilder.setDatabase(parent.getParent().getDatabase());
        submapInitBuilder.setName(Utils.getNameFromFile(map_));

        for (File smp_ : Objects.requireNonNull(map_.listFiles())) {
            if (smp_.getName().endsWith(Constant.LAYER_PREFIX)) {
                submapInitBuilder.addLayer(smp_);
            } else if (smp_.getName().equals(Constant.CONFIG_FILE_NAME
                    + Constant.DOT_PREFIX
                    + Constant.JSON_PREFIX)) {
                Map<String, Config> configs = (Map<String, Config>) JsonSerializer.deserialize(Utils.getText(smp_),
                        TypeToken.getParameterized(HashMap.class, String.class, Config.class));
                submapInitBuilder.setConfigs(configs);
            }
        }
        elementManager.addElement(submapInitBuilder.build());
    }

    public List<MBRectangle_c> getElements(MBRectangle_c scope) {
        return elementManager.getElements(scope);
    }
}

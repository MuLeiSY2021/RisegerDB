package org.riseger.main.cache.manager;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.othertree.RStarTree;
import pers.muleisy.rtree.othertree.RTree;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.util.List;

@Data
public class ElementManager {

    private static final Logger LOG = Logger.getLogger(ElementManager.class);
    private final RTree<MBRectangle_c> rtreeKeyIndex;
    private final Layer_c parent;

    public ElementManager(RTree<MBRectangle_c> rtreeKeyIndex, Layer_c parent) {
        this.rtreeKeyIndex = rtreeKeyIndex;
        this.parent = parent;
    }

    public static ElementManager buildRStartElementManager(int nodeSize, double threshold, Layer_c layerC, Class clazz) {
        return new ElementManager(new RStarTree<>(nodeSize, threshold, clazz), layerC);
    }

    public static ElementManager deserializeRStartElementManager(Layer_c layerC, File layer_) {
        try {
            return new ElementManager((RTree<MBRectangle_c>) RTree.deserializeStar(Utils.fileToByteBuf(layer_)), layerC);
        } catch (Exception e) {
            LOG.error("An Exception occur", e);
        }
        return null;
    }

    public void addElement(Element e) {
        Element_c e_c = new Element_c(e,
                parent.getParent().getParent().getDatabase(),
                this,
                parent.getParent().getParent().getThreshold());
        rtreeKeyIndex.insert(e_c);
        parent.expand(e_c);
    }

    public void addElement(MapDB_c e) {
        rtreeKeyIndex.insert(e);
        parent.expand(e);
    }

    public void updateIndex(MBRectangle_c self, Rectangle rectangle) {
        if (self.willBeExpand(rectangle)) {
            rtreeKeyIndex.deleteStrict(self);
            self.expand(rectangle);
            rtreeKeyIndex.insert(self);
            this.parent.expand(self);
        }
    }

    public void remove(MapDB_c mapDBC) {
        rtreeKeyIndex.deleteStrict(mapDBC);
    }

    public List<MBRectangle_c> getElements(MBRectangle_c scope) {
        return rtreeKeyIndex.search(scope);
    }
}

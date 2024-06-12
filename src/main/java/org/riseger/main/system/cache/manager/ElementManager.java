package org.riseger.main.system.cache.manager;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.system.cache.LockableEntity;
import org.riseger.main.system.cache.component.Element;
import org.riseger.main.system.cache.component.GeoRectangle;
import org.riseger.main.system.cache.component.Layer;
import org.riseger.protocol.struct.entity.Element_p;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.othertree.RStarTree;
import pers.muleisy.rtree.othertree.RTree;
import pers.muleisy.rtree.rectangle.Rectangle;

import java.io.File;
import java.util.List;

@Data
public class ElementManager extends LockableEntity {

    private static final Logger LOG = Logger.getLogger(ElementManager.class);
    private final RTree<GeoRectangle> rtreeKeyIndex;
    private final Layer parent;

    public ElementManager(RTree<GeoRectangle> rtreeKeyIndex, Layer parent) {
        this.rtreeKeyIndex = rtreeKeyIndex;
        this.parent = parent;
    }

    public static ElementManager buildRStartElementManager(int nodeSize, double threshold, Layer layerC, Class clazz) {
        return new ElementManager(new RStarTree<>(nodeSize, threshold, clazz), layerC);
    }

    public static ElementManager deserializeRStartElementManager(Layer layerC, File layer_) {
        try {
            return new ElementManager((RTree<GeoRectangle>) RTree.deserializeStar(Utils.fileToByteBuf(layer_)), layerC);
        } catch (Exception e) {
            LOG.error("An Exception occur", e);
        }
        return null;
    }

    public void addElement(Element_p e) {
        super.write();
        Element e_c = new Element(e,
                parent.getParent().getParent().getDatabase(),
                this,
                parent.getParent().getParent().getThreshold());
        rtreeKeyIndex.insert(e_c);
        super.unwrite();
    }

    public void setIndex(GeoRectangle self, Rectangle rectangle) throws CloneNotSupportedException {
        super.write();
        if (rtreeKeyIndex.willShrunk((GeoRectangle) rectangle)) {
            rtreeKeyIndex.deleteStrict(self);
            self.setAll(rectangle);
            rtreeKeyIndex.insert(self);
        }
        super.unwrite();
    }

    public List<GeoRectangle> getElements(GeoRectangle scope) {
        super.read();
        List<GeoRectangle> elements = rtreeKeyIndex.search(scope);
        super.unread();
        return elements;
    }

}

package org.riseger.main.cache.manager;

import lombok.Data;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.protoctl.struct.entity.Element;
import org.riseger.utils.Utils;
import pers.muleisy.rtree.othertree.RStarTree;
import pers.muleisy.rtree.othertree.RTree;

import java.io.File;

@Data
public class ElementManager {

    private final RTree<MBRectangle_c> rtreeKeyIndex;

    private final Layer_c parent;

    public ElementManager(RTree<MBRectangle_c> rtreeKeyIndex, Layer_c parent) {
        this.rtreeKeyIndex = rtreeKeyIndex;
        this.parent = parent;
    }

    public static ElementManager buildRStartElementManager(int nodeSize, double threshold, Layer_c layerC) {
        return new ElementManager(new RStarTree<>(nodeSize, threshold), layerC);
    }

    public static ElementManager deserializeRStartElementManager(Layer_c layerC, File layer_) throws Exception {
        return new ElementManager((RTree<MBRectangle_c>) RTree.deserializeStar(Utils.fileToByteBuf(layer_)), layerC);
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
    }

    public void updateIndex(MBRectangle_c mbr) {
        rtreeKeyIndex.deleteStrict(mbr);
        rtreeKeyIndex.insert(mbr);
    }

    public void remove(MapDB_c mapDBC) {
        rtreeKeyIndex.deleteStrict(mapDBC);
    }

}

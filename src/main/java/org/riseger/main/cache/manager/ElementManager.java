package org.riseger.main.cache.manager;

import lombok.Data;
import org.riseger.main.cache.entity.component.map.Layer_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;
import org.riseger.main.cache.entity.component.mbr.Element_c;
import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.protoctl.struct.entity.Element;
import pers.muleisy.rtree.othertree.RStarTree;
import pers.muleisy.rtree.othertree.RTree;

@Data
public class ElementManager {

    private final RTree<MBRectangle_c> rtreeKeyIndex;

    private final Layer_c parent;

    public ElementManager(RTree<MBRectangle_c> rtreeKeyIndex, Layer_c parent) {
        this.rtreeKeyIndex = rtreeKeyIndex;
        this.parent = parent;
    }

    public static ElementManager buildRStartElementManager(int nodeSize, double threshold, Layer_c layerC) {
        return new ElementManager(new RStarTree<>(nodeSize, threshold),layerC);
    }

    public void addElement(Element e) {
        Element_c e_c = new Element_c(e,
                parent.getParent().getParent().getParent().getDatabase(),
                this,
                parent.getParent().getParent().getThreshold());
        rtreeKeyIndex.insert(e_c);
        parent.expand(e_c);
    }
    public void addElement(MBRectangle_c e) {
        rtreeKeyIndex.insert(e);
        parent.expand(e);
    }

    public void updateIndex(MBRectangle_c mbr) {
        rtreeKeyIndex.deleteStrict(mbr);
        rtreeKeyIndex.insert(mbr);
    }

    public void remove(MapDB_c mapDBC) {
        rtreeKeyIndex.deleteStrict(mapDBC);
    }


}

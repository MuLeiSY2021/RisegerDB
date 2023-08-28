package org.riseger.main.cache.entity.manager;

import org.riseger.main.cache.entity.component.map.Layer_c;
import org.riseger.main.cache.entity.component.map.MapDB_c;
import org.riseger.main.cache.entity.component.mbr.Element_c;
import org.riseger.main.cache.entity.component.mbr.MBRectangle_c;
import org.riseger.protoctl.struct.entity.Element;
import pers.muleisy.rtree.othertree.RStarTree;
import pers.muleisy.rtree.othertree.RTree;
import pers.muleisy.rtree.rectangle.MBRectangle;

public class ElementManager {

    private final RTree<MBRectangle_c> rtreeKeyIndex;

    private final Layer_c layer;

    public ElementManager(RTree<MBRectangle_c> rtreeKeyIndex, Layer_c layerC) {
        this.rtreeKeyIndex = rtreeKeyIndex;
        this.layer = layerC;
    }

    public static ElementManager buildRStartElementManager(int nodeSize, double threshold, Layer_c layerC) {
        return new ElementManager(new RStarTree<>(nodeSize, threshold),layerC);
    }

    public void addElement(Element e) {
        Element_c e_c = new Element_c(e,
                layer.getLayerManager().getMap().getMapManager().getDatabase(),
                this,
                layer.getLayerManager().getMap().getThreshold());
        rtreeKeyIndex.insert(e_c);
        layer.expand(e_c);
    }
    public void addElement(MBRectangle_c e) {
        rtreeKeyIndex.insert(e);
        layer.expand(e);
    }

    public void updateIndex(MBRectangle_c mbr) {
        rtreeKeyIndex.deleteStrict(mbr);
        rtreeKeyIndex.insert(mbr);
    }

    public void remove(MapDB_c mapDBC) {
        rtreeKeyIndex.deleteStrict(mapDBC);
    }
}

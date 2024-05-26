package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.cache.component.GeoRectangle;
import org.riseger.main.system.cache.component.Layer;
import org.riseger.main.system.compile.compoent.SearchSet;

import java.util.*;

public class WhereIterator {
    private final Map<String, SearchSet> searchMap;

    private final List<GeoMap> maps;

    private final GeoRectangle scope;

    private final Iterator<SearchSet> searchListIterator;

    private SearchSet model;

    private ListIterator<Layer> layerListIterator;

    private Layer layer;

    private ListIterator<GeoRectangle> elementListIterator;

    public WhereIterator(Map<String, SearchSet> searchMap, List<GeoMap> maps, GeoRectangle scope) {
        this.searchMap = searchMap;
        this.maps = maps;
        this.scope = scope;
        this.searchListIterator = searchMap.values().iterator();

        this.model = searchListIterator.next();

        this.layerListIterator = findModelLayer(searchMap.get(model.getName()), maps, scope).listIterator();
        this.layer = layerListIterator.next();

        this.elementListIterator = layer.getElements(scope).listIterator();
    }

    public GeoRectangle next() {
        return this.elementListIterator.next();
    }

    public boolean hasNext() {
        if (!this.elementListIterator.hasNext()) {
            if (!layerListIterator.hasNext() && searchListIterator.hasNext()) {
                this.model = searchListIterator.next();
                this.layerListIterator = findModelLayer(searchMap.get(model.getName()), maps, scope).listIterator();
            }
            if (layerListIterator.hasNext()) {
                this.layer = layerListIterator.next();
                this.elementListIterator = layer.getElements(scope).listIterator();
            }
        }
        return this.elementListIterator.hasNext();
    }

    private List<Layer> findModelLayer(SearchSet model, List<GeoMap> maps, GeoRectangle scope) {
        List<Layer> results = new LinkedList<>();

        for (List<String> route : model.getChild()) {
            /*
              province_scope.area_scope.building_model
              province_scope.building_model
            */
            List<GeoMap> tmpLayers = new LinkedList<>(maps),
                    nextLayers = new LinkedList<>();
            for (String layerName : route) {
                /*
                  province_scope
                  area_scope
                  building_model
                 */
                for (GeoMap map : tmpLayers) {
                    /*
                      Tianjin
                      Shanghai
                      Beijing
                     */
                    List<GeoRectangle> tmp = map.getSubmapLayer(layerName).getElements(scope);
                    for (GeoRectangle mbr : tmp) {
                        nextLayers.add((GeoMap) mbr);
                    }
                }
                tmpLayers = nextLayers;
                nextLayers = new LinkedList<>();
            }
            for (GeoMap map : tmpLayers) {
                results.add(map.getElementLayer(model.getName()));
            }
        }
        return results;
    }
}
package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.compiler.compoent.SearchSet;

import java.util.*;

public class WhereIterator {
    private final Map<String, SearchSet> searchMap;

    private final List<MapDB_c> maps;

    private final MBRectangle_c scope;

    private final Iterator<SearchSet> searchListIterator;

    private SearchSet model;

    private ListIterator<Layer_c> layerListIterator;

    private Layer_c layer;

    private ListIterator<MBRectangle_c> elementListIterator;

    public WhereIterator(Map<String, SearchSet> searchMap, List<MapDB_c> maps, MBRectangle_c scope) {
        this.searchMap = searchMap;
        this.maps = maps;
        this.scope = scope;
        this.searchListIterator = searchMap.values().iterator();

        this.model = searchListIterator.next();

        this.layerListIterator = findModelLayer(searchMap.get(model.getName()), maps, scope).listIterator();
        this.layer = layerListIterator.next();

        this.elementListIterator = layer.getElements(scope).listIterator();
    }

    public MBRectangle_c next() {
        if (!this.elementListIterator.hasNext()) {
            if (!layerListIterator.hasNext()) {
                this.model = searchListIterator.next();
                this.layerListIterator = findModelLayer(searchMap.get(model.getName()), maps, scope).listIterator();
            }
            this.layer = layerListIterator.next();
            this.elementListIterator = layer.getElements(scope).listIterator();
        }
        return this.elementListIterator.next();
    }

    public boolean hasNext() {
        return elementListIterator.hasNext();
    }

    private List<Layer_c> findModelLayer(SearchSet model, List<MapDB_c> maps, MBRectangle_c scope) {
        List<Layer_c> results = new LinkedList<>();

        for (String[] route : model.getChild()) {
            /*
              province_scope.area_scope.building_model
              province_scope.building_model
            */
            List<MapDB_c> tmpLayers = new LinkedList<>(maps),
                    nextLayers = new LinkedList<>();
            for (String layerName : route) {
                /*
                  province_scope
                  area_scope
                  building_model
                 */
                for (MapDB_c map : tmpLayers) {
                    /*
                      Tianjin
                      Shanghai
                      Beijing
                     */
                    List<MBRectangle_c> tmp = map.getSubmapLayer(layerName).getElements(scope);
                    for (MBRectangle_c mbr : tmp) {
                        nextLayers.add((MapDB_c) mbr);
                    }
                    tmpLayers = nextLayers;
                    nextLayers = new LinkedList<>();
                }
            }
            for (MapDB_c map : tmpLayers) {
                results.add(map.getElementLayer(model.getName()));
            }
        }
        return results;
    }
}
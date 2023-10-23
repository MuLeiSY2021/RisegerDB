package org.riseger.main.sql.function.main;

import lombok.Data;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.compoent.SearchSet;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Where_fc extends MainFunction_c {

    public Where_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        if (!super.hasMap(MemoryConstant.WHERE)) {
            List<MapDB_c> maps = new LinkedList<>();

            MBRectangle_c scope = (MBRectangle_c) getMap(MemoryConstant.SCOPE);//√

            Map<String, SearchSet> searchMap = (Map<String, SearchSet>) getMap(MemoryConstant.MODEL);//√

            List<SearchSet> searchList = (List<SearchSet>) getMap(MemoryConstant.SEARCH);//√
            setMap(new WhereIterator(searchMap, maps, scope, searchList.listIterator()), MemoryConstant.WHERE);
        }
        WhereIterator iterator = (WhereIterator) getMap(MemoryConstant.WHERE);
        if (iterator.hasNext()) {
            if (super.hasMap(MemoryConstant.ELEMENT) &&
                    super.hasMap(MemoryConstant.PASS) && (Boolean) super.getMap(MemoryConstant.PASS)) {
                if (!super.hasMap(MemoryConstant.RESULT)) {
                    setMap(new LinkedList<Element_c>(), MemoryConstant.RESULT);
                }
                ((LinkedList<Element_c>) getMap(MemoryConstant.RESULT)).add((Element_c) super.getMap(MemoryConstant.ELEMENT));
            }
            MBRectangle_c mbRectangle = iterator.next();
            super.setMap(mbRectangle, MemoryConstant.ELEMENT);
        }

    }

    @Data
    private static class WhereIterator {
        private final Map<String, SearchSet> searchMap;

        private final List<MapDB_c> maps;

        private final MBRectangle_c scope;

        private final ListIterator<SearchSet> searchListIterator;

        private SearchSet model;

        private ListIterator<Layer_c> layerListIterator;

        private Layer_c layer;

        private ListIterator<MBRectangle_c> elementListIterator;

        public WhereIterator(Map<String, SearchSet> searchMap, List<MapDB_c> maps, MBRectangle_c scope, ListIterator<SearchSet> searchListIterator) {
            this.searchMap = searchMap;
            this.maps = maps;
            this.scope = scope;
            this.searchListIterator = searchListIterator;

            this.model = searchListIterator.next();

            this.layerListIterator = findModelLayer(searchMap.get(model.getName()), maps, scope).listIterator();
            this.layer = layerListIterator.next();

            this.elementListIterator = layer.getElements(scope).listIterator();
        }

        public MBRectangle_c next() {
            if (!this.elementListIterator.hasNext()) {
                if (!searchListIterator.hasNext()) {
                    throw new IndexOutOfBoundsException("Search list is done");
                } else if (!layerListIterator.hasNext()) {
                    this.model = searchListIterator.next();
                    this.layerListIterator = findModelLayer(searchMap.get(model.getName()), maps, scope).listIterator();
                }
                this.layer = layerListIterator.next();
                this.elementListIterator = layer.getElements(scope).listIterator();
            }
            return this.elementListIterator.next();
        }

        public boolean hasNext() {
            return searchListIterator.hasNext();
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
}

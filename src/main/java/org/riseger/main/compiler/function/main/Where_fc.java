package org.riseger.main.compiler.function.main;

import lombok.Data;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.Layer_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.compoent.SearchSet;
import org.riseger.main.compiler.function.type.MainFunction_c;
import org.riseger.main.compiler.semantic.SemanticTree;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.compiler.function.Entity_f;
import org.riseger.protoctl.compiler.function.ProcessorFunction;
import org.riseger.protoctl.compiler.function.loop.Back_f;
import org.riseger.protoctl.compiler.function.loop.IfJump_f;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;

import java.util.*;

public class Where_fc extends MainFunction_c implements ProcessorFunction {

    public Where_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        if (!super.hasMap(MemoryConstant.WHERE)) {
            List<MapDB_c> maps = new LinkedList<>();

            MBRectangle_c scope = (MBRectangle_c) getMap(MemoryConstant.SCOPE);//√

            Map<String, SearchSet> searchMap = (Map<String, SearchSet>) getMap(MemoryConstant.MODEL);//√

            setMap(new WhereIterator(searchMap, maps, scope), MemoryConstant.WHERE);
        }
        WhereIterator iterator = (WhereIterator) getMap(MemoryConstant.WHERE);
        if (iterator.hasNext()) {
            if (super.hasMap(MemoryConstant.ELEMENT) &&
                    //已通过函数校验者
                    super.hasMap(MemoryConstant.PASS) && (Boolean) super.getMap(MemoryConstant.PASS)) {
                if (!super.hasMap(MemoryConstant.RESULT)) {
                    setMap(new ResultSet(), MemoryConstant.RESULT);
                }
                Element_c element = (Element_c) super.getMap(MemoryConstant.ELEMENT);
                ResultSet resultSet;
                if (hasMap(MemoryConstant.RESULT)) {
                    resultSet = (ResultSet) super.getMap(MemoryConstant.RESULT);
                } else {
                    resultSet = new ResultSet();
                    super.setMap(resultSet, MemoryConstant.RESULT);
                }
                ResultModelSet resultModelSet;
                if (resultSet.hasModelSet(element.getModel())) {
                    resultModelSet = resultSet.getModelSet(element.getModel());
                } else {
                    resultModelSet = new ResultModelSet();
                    resultSet.setModelSet(element.getModel(), resultModelSet);
                }
                Map<String, List<String>> searchList = (Map<String, List<String>>) getMap(MemoryConstant.SEARCH);//√

                ResultElement resultElement = new ResultElement();
                resultElement.setKeyColumns(element.getCoordsSet());
                for (String column : searchList.get(element.getModel())) {
                    resultElement.addColumn(column, element.getAttributes().get(column));
                }
                resultModelSet.add(new ResultElement());
            }
            MBRectangle_c mbRectangle = iterator.next();
            super.setMap(mbRectangle, MemoryConstant.ELEMENT);
            super.put(false);
        }
        super.put(true);
    }

    @Override
    public void preHandle(SemanticTree.Node node, int size) {
        node.addHead(new Back_f());
        node.addHead(new Entity_f(size));
        node.addHead(new IfJump_f());
        node.addHead(new Entity_f(node.getLevel() + 1));

    }

    @Data
    private static class WhereIterator {
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

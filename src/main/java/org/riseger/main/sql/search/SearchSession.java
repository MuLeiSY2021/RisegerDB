package org.riseger.main.sql.search;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.*;
import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.compiler.semantic.SQLTree;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.main.sql.function.type.Function_c;
import org.riseger.main.sql.function.type.RectangleFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.command.USE;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONBLE;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.*;

public class SearchSession {
    private static final Logger LOG = Logger.getLogger(SearchSession.class);

    //USE
    public final Database_c database;

    public final List<MapDB_c> maps = new LinkedList<>();

    public final MBRectangle_c scope;

    public final Map<String, SearchSet> models;

    //Select
    private final List<SearchSet> searches;

    //Where
    private final Queue<Function_c<?>> functionQueue;

    private final SearchMemory memory;

    public SearchSession(USE sql) throws Exception {

        //Deal Use
        this.database = CacheMaster.INSTANCE.getDatabase(sql.getDatabase());
        this.memory = new SearchMemory();
        this.maps.add(this.database.getMap(sql.getMap()));
        this.scope = processScope(sql.getScope());
        this.models = dealModel(sql.getModels());

        //Deal Select
        this.searches = dealSelect(sql.getSearch().getContent());

        //Deal Where
        SQLTree tree = new SQLTree(sql.getSearch().getWhere(), memory, maps.get(0).getThreshold());
        this.functionQueue = tree.genFunctionList();
    }

    private Map<String, SearchSet> dealModel(List<String> models) {
        Map<String, SearchSet> searchSets = new HashMap<>();
        Map<String, SearchSet> searchSetMap = new HashMap<>();
        for (String list : models) {
            String[] sets = list.split("\\.");
            String modelName = sets[sets.length - 1];
            sets = Arrays.copyOfRange(sets, 0, sets.length - 1);
            setSearchSets(searchSets, searchSetMap, sets, modelName);
        }
        return searchSets;
    }

    private List<SearchSet> dealSelect(List<String> models) {
        List<SearchSet> searchSets = new LinkedList<>();
        Map<String, SearchSet> searchSetMap = new HashMap<>();
        for (String list : models) {
            String[] sets = list.split("\\.");
            String modelName = sets[0];
            sets = Arrays.copyOfRange(sets, 1, sets.length);
            setSearchSets(searchSets, searchSetMap, sets, modelName);
        }
        return searchSets;
    }

    private void setSearchSets(Map<String, SearchSet> searchSets, Map<String, SearchSet> searchSetMap, String[] sets, String modelName) {
        SearchSet searchSet;
        if (searchSetMap.containsKey(modelName)) {
            searchSet = searchSetMap.get(modelName);
            searchSet.add(sets);

        } else {
            searchSet = new SearchSet(modelName, sets);
            searchSetMap.put(modelName, searchSet);
            searchSets.put(searchSet.name, searchSet);
        }
    }

    private void setSearchSets(List<SearchSet> searchSets, Map<String, SearchSet> searchSetMap, String[] sets, String modelName) {
        SearchSet searchSet;
        if (searchSetMap.containsKey(modelName)) {
            searchSet = searchSetMap.get(modelName);
            searchSet.add(sets);

        } else {
            searchSet = new SearchSet(modelName, sets);
            searchSetMap.put(modelName, searchSet);
            searchSets.add(searchSet);
        }
    }

    public MBRectangle_c processScope(RECTANGLE_FUNCTIONBLE rectangleFunction) throws Exception {

        Queue<Function_c<?>> tmpFunctionQueue = new SQLTree(rectangleFunction, memory, maps.get(0).getThreshold()).genFunctionList();
        for (Function_c<?> function : tmpFunctionQueue) {
            if (function instanceof RectangleFunction_c) {
                return ((RectangleFunction_c) function).resolve(null);
            } else {
                function.resolve(null);
            }
        }
        throw new IllegalArgumentException("Invalid function");
    }

    public Map<String, List<Element_c>> process() throws Exception {
        Map<String, List<Element_c>> results = new HashMap<>();
        for (SearchSet searchSet : this.searches) {
            List<Element_c> result = new LinkedList<>();
            List<Layer_c> layers = findModelLayer(this.models.get(searchSet.name));
            for (Layer_c layer : layers) {
                for (MBRectangle_c mbr : layer.getElements(scope)) {
                    if (mbr instanceof Element_c && compileProcessor((Element_c) mbr)) {
                        result.add((Element_c) mbr);
                    } else if (mbr instanceof MapDB_c) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            results.put(searchSet.name, result);
        }
        LOG.debug("result: " + JsonSerializer.serializeToString(results));

        return results;
    }

    private List<Element_c> filter(List<Element_c> result) {
        //TODO: 做结果的筛查和Result返回
        return result;
    }

    public boolean compileProcessor(Element_c element) throws IllegalSearchAttributeException {
        boolean passed = false;
        for (Function_c<?> function : functionQueue) {
            if (function instanceof BooleanFunction_c) {
                passed = ((BooleanFunction_c) function).resolve(element);
            } else {
                function.resolve(element);
            }
        }
        return passed;
    }

    private List<Layer_c> findModelLayer(SearchSet model) {
        List<Layer_c> results = new LinkedList<>();

        for (String[] route : model.child) {
            /*
              province_scope.area_scope.building_model
              province_scope.building_model
            */
            List<MapDB_c> tmpLayers = new LinkedList<>(this.maps),
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
                results.add(map.getElementLayer(model.name));
            }
        }
        return results;
    }

    @Data
    private static class SearchSet {
        String name;

        List<String[]> child = new LinkedList<>();

        public SearchSet(String name, String[] sets) {
            this.name = name;
            this.child.add(sets);
        }

        public void add(String[] sets) {
            child.add(Arrays.copyOfRange(sets, 1, sets.length));
        }
    }
}

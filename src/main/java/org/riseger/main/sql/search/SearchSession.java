package org.riseger.main.sql.search;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.*;
import org.riseger.main.compiler.semantic.SQLTree;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.main.sql.function.type.Function_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.command.USE;
import org.riseger.protoctl.search.command.WHERE;
import org.riseger.protoctl.serializer.JsonSerializer;

import java.util.*;

public class SearchSession {
    private static final Logger LOG = Logger.getLogger(SearchSession.class);


    public SearchSession(USE sql, Database_c database, MBRectangle_c scope, Map<String, SearchSet> models, List<SearchSet> searches, SearchMemory memory) throws Exception {

        //Deal Where

    }


    public Map<String, List<Element_c>> process() throws Exception {

        List<MapDB_c> maps = new LinkedList<>();

        MBRectangle_c scope = null;//√

        Map<String, SearchSet> searchMap = null;//√

        //Select
        List<SearchSet> searchList = null;//√

        //Where
        SearchMemory memory = null;
        SQLTree tree = new SQLTree(new WHERE(), memory, maps.get(0).getThreshold());
        Queue<Function_c> functionQueue = tree.genFunctionList();

        Map<String, List<Element_c>> results = new HashMap<>();
        for (SearchSet searchSet : searchList) {
            List<Element_c> result = new LinkedList<>();
            List<Layer_c> layers = findModelLayer(searchMap.get(searchSet.name), maps, scope);
            for (Layer_c layer : layers) {
                for (MBRectangle_c mbr : layer.getElements(scope)) {
                    if (mbr instanceof Element_c && compileProcessor((Element_c) mbr, functionQueue)) {
                        result.add((Element_c) mbr);
                    } else if (mbr instanceof MapDB_c) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            results.put(searchSet.name, filter(result));
        }
        LOG.debug("result: " + JsonSerializer.serializeToString(results));

        return results;
    }

    private List<Element_c> filter(List<Element_c> result) {
        //TODO: 做结果的筛查和Result返回
        return result;
    }

    public boolean compileProcessor(Element_c element, Queue<Function_c> functionQueue) throws IllegalSearchAttributeException {
        boolean passed = false;
        for (Function_c function : functionQueue) {
            if (function instanceof BooleanFunction_c) {
                passed = ((BooleanFunction_c) function).resolve(element);
            } else {
                function.resolve(element);
            }
        }
        return passed;
    }

    private List<Layer_c> findModelLayer(SearchSet model, List<MapDB_c> maps, MBRectangle_c scope) {
        List<Layer_c> results = new LinkedList<>();

        for (String[] route : model.child) {
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
                results.add(map.getElementLayer(model.name));
            }
        }
        return results;
    }
}

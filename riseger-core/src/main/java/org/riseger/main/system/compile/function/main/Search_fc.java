package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.Element;
import org.riseger.main.system.compile.clazz.DotString;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.compiler.result.ResultElement;
import org.riseger.protocol.compiler.result.ResultModelSet;
import org.riseger.protocol.compiler.result.ResultSet;
import org.riseger.protocol.exception.SQLException;

import java.util.*;

public class Search_fc extends Function_c implements WhereHandleFunction {
    public Search_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        List<DotString> models = (List<DotString>) searchMemory.poll();
        Map<String, List<String>> searchSetMap = new HashMap<>();
        Map<String, Map<String, Object>> expireCopys = new HashMap<>();
        for (DotString dotString : models) {
            String modelName = dotString.getParent(),
                    column = dotString.getBottom();
            List<String> searchSetList;
            Map<String, Object> expireCopy;
            if (searchSetMap.containsKey(modelName)) {
                searchSetList = searchSetMap.get(modelName);
                expireCopy = expireCopys.get(modelName);
            } else {
                searchSetList = new LinkedList<>();
                searchSetMap.put(modelName, searchSetList);
                expireCopy = new HashMap<>();
                expireCopys.put(modelName, expireCopy);
            }
            if (!expireCopy.containsKey(column)) {
                searchSetList.add(column);
                expireCopy.put(column, null);
            }
        }

        searchMemory.setMap(searchSetMap, MemoryConstant.METOD_PARAMATER);
        searchMemory.setMap(this, MemoryConstant.METOD_PROCESS);
    }

    @Override
    public void postProcess(SearchMemory searchMemory, CommandList commandList) {
        if (!searchMemory.hasMap(MemoryConstant.RESULT)) {
            searchMemory.setMap(new ResultSet(), MemoryConstant.RESULT);
        }
        Element element = (Element) searchMemory.get(MemoryConstant.ELEMENT);
        ResultSet resultSet;
        if (searchMemory.hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) searchMemory.get(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            searchMemory.setMap(resultSet, MemoryConstant.RESULT);
        }
        ResultModelSet resultModelSet;
        if (resultSet.hasModelSet(element.getModel())) {
            resultModelSet = resultSet.getModelSet(element.getModel());
        } else {
            resultModelSet = new ResultModelSet();
            resultSet.setModelSet(element.getModel(), resultModelSet);
        }
        Map<String, List<String>> searchList = (Map<String, List<String>>) searchMemory.get(MemoryConstant.METOD_PARAMATER);//√

        ResultElement resultElement = new ResultElement();
        for (String column : searchList.get(element.getModel())) {
            if (column.startsWith("KEY_LOOP")) {
                String[] values = column.split("::");
                if (values.length == 1) {
                    resultElement.setAllKeyColumns(element.getCoordsSet());
                } else if (values.length == 2) {
                    int index = Integer.parseInt(values[1]);
                    resultElement.setKeyColumns(index, element.getCoordsSet()[index]);
                } else if (values.length == 3) {
                    int index = Integer.parseInt(values[1]),
                            x = Objects.equals(values[2], "x") ? 0 : 1;
                    resultElement.setKeyColumns(index, x, element.getCoordsSet()[index][x]);
                }
            } else {
                resultElement.addColumn(column, element.getAttributes().get(column));
            }
        }
        resultModelSet.add(resultElement);
    }
}

package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.compile.clazz.DotString;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Search_fc extends Function_c {
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

        searchMemory.setMap(searchSetMap, MemoryConstant.SEARCH);
    }
}

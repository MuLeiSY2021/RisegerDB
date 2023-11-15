package org.riseger.main.compiler.function.main;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Search_fc extends Function_c {
    public Search_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        List<String> models = (List<String>) poll();
        Map<String, List<String>> searchSetMap = new HashMap<>();
        Map<String, Map<String, Object>> expireCopys = new HashMap<>();
        for (String list : models) {
            String[] sets = list.split("\\.");
            String modelName = sets[0],
                    column = sets[1];
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

        setMap(searchSetMap, MemoryConstant.SEARCH);
    }
}

package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.compoent.SearchSet;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseModel_fc extends Function_c {

    public UseModel_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        List<String> models = (List<String>) searchMemory.poll();
        Map<String, SearchSet> searchSets = new HashMap<>();
        Map<String, SearchSet> searchSetMap = new HashMap<>();
        for (String list : models) {
            String[] sets = list.split("\\.");
            String modelName = sets[sets.length - 1];
            sets = Arrays.copyOfRange(sets, 0, sets.length - 1);
            SearchSet searchSet;
            if (searchSetMap.containsKey(modelName)) {
                searchSet = searchSetMap.get(modelName);
                searchSet.add(sets);

            } else {
                searchSet = new SearchSet(modelName, sets);
                searchSetMap.put(modelName, searchSet);
                searchSets.put(searchSet.getName(), searchSet);
            }
        }
        searchMemory.setMap(searchSets, MemoryConstant.MODEL);
    }


}

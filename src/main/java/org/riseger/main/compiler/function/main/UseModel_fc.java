package org.riseger.main.compiler.function.main;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.compoent.SearchSet;
import org.riseger.main.compiler.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseModel_fc extends MainFunction_c {

    public UseModel_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        List<String> models = (List<String>) poll();
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
        super.setMap(searchSets, MemoryConstant.MODEL);
    }


}

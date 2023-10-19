package org.riseger.main.sql.function.main;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.MemoryConstant;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.compoent.SearchSet;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

import java.util.*;

public class Search_fc extends MainFunction_c {
    public Search_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        List<String> models = (List<String>) poll();
        List<SearchSet> searchSets = new LinkedList<>();
        Map<String, SearchSet> searchSetMap = new HashMap<>();
        for (String list : models) {
            String[] sets = list.split("\\.");
            String modelName = sets[0];
            sets = Arrays.copyOfRange(sets, 1, sets.length);
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

        setMap(searchSets, MemoryConstant.SEARCH);
    }
}

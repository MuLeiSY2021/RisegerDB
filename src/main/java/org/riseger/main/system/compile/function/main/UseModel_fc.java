package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.Model;
import org.riseger.main.system.compile.clazz.DotString;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.compoent.SearchSet;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseModel_fc extends Function_c {

    public UseModel_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        List<DotString> dotStrings = (List<DotString>) searchMemory.poll();
        Map<String, SearchSet> searchSets = new HashMap<>();
        Map<String, SearchSet> searchSetMap = new HashMap<>();
        Map<String, Model> models = new HashMap<>();
        for (DotString dotString : dotStrings) {
            String modelName = dotString.getBottom();
            List<String> sets = dotString.getParents();
            SearchSet searchSet;
            if (searchSetMap.containsKey(modelName)) {
                searchSet = searchSetMap.get(modelName);
                searchSet.add(sets);
            } else {
                searchSet = new SearchSet(modelName, sets);
                searchSetMap.put(modelName, searchSet);
                searchSets.put(searchSet.getName(), searchSet);
            }
            if (!models.containsKey(modelName)) {
                Database database = (Database) searchMemory.get(MemoryConstant.DATABASE);
                Model model = database.getModelManager().getModel(modelName);
                models.put(modelName, model);
            }
        }
        searchMemory.setMap(searchSets, MemoryConstant.SEARCH_SETS);
        searchMemory.setMap(models, MemoryConstant.MODELS);
    }


}

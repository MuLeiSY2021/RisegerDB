package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.entity.component.MBRectangle_c;
import org.riseger.main.system.cache.entity.component.MapDB_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.compoent.SearchSet;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PreWhere_fc extends Function_c {

    public PreWhere_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        WhereIterator iterator;
        List<MapDB_c> maps = new LinkedList<>();
        maps.add((MapDB_c) searchMemory.get(MemoryConstant.MAP));
        MBRectangle_c scope = (MBRectangle_c) searchMemory.get(MemoryConstant.SCOPE);//√

        Map<String, SearchSet> searchMap = (Map<String, SearchSet>) searchMemory.get(MemoryConstant.MODEL);//√
        iterator = new WhereIterator(searchMap, maps, scope);
        searchMemory.setMap(iterator, MemoryConstant.WHERE);
        if (iterator.hasNext()) {
            MBRectangle_c mbRectangle = iterator.next();
            searchMemory.setMap(mbRectangle, MemoryConstant.ELEMENT);
        }
    }
}

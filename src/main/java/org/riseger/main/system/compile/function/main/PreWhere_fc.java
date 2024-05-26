package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.cache.component.GeoRectangle;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.compoent.SearchSet;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

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
        List<GeoMap> maps = new LinkedList<>();
        maps.add((GeoMap) searchMemory.get(MemoryConstant.MAP));
        GeoRectangle scope = (GeoRectangle) searchMemory.get(MemoryConstant.SCOPE);//√

        iterator = new WhereIterator((Map<String, SearchSet>) searchMemory.get(MemoryConstant.SEARCH_SETS), maps, scope);
        searchMemory.setMap(iterator, MemoryConstant.WHERE);
        if (iterator.hasNext()) {
            GeoRectangle geoRectangle = iterator.next();
            searchMemory.setMap(geoRectangle, MemoryConstant.ELEMENT);
        }
    }
}

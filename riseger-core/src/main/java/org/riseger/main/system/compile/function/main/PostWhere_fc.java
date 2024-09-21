package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.GeoRectangle;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class PostWhere_fc extends Function_c {

    public PostWhere_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        WhereIterator iterator = (WhereIterator) searchMemory.get(MemoryConstant.WHERE);
        GeoRectangle geoRectangle = iterator.next();
        searchMemory.setMap(geoRectangle, MemoryConstant.ELEMENT);
    }

}

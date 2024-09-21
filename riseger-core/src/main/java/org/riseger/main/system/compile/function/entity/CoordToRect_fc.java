package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.cache.component.Coord;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class CoordToRect_fc extends Function_c {

    public CoordToRect_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Coord result = (Coord) searchMemory.poll();
        searchMemory.push(result.toRectangle(((Database) searchMemory.get(MemoryConstant.DATABASE)).getThreshold()));
    }
}

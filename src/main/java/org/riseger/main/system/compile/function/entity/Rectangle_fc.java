package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.cache.component.Coord_c;
import org.riseger.main.system.cache.component.MBRectangle_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Rectangle_fc extends Function_c {

    public Rectangle_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Double len = ((Number) searchMemory.poll()).doubleValue();
        Coord_c coord = (Coord_c) searchMemory.poll();
        MBRectangle_c mbr = new MBRectangle_c(coord, len, (Double) searchMemory.get(MemoryConstant.THRESHOLD));
        searchMemory.push(mbr);
    }
}

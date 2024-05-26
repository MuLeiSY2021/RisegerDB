package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.cache.component.Coord;
import org.riseger.main.system.cache.component.GeoRectangle;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Rectangle_fc extends Function_c {

    public Rectangle_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Double len = ((Number) searchMemory.poll()).doubleValue();
        Coord coord = (Coord) searchMemory.poll();
        GeoRectangle mbr = new GeoRectangle(coord, len, (Double) searchMemory.get(MemoryConstant.THRESHOLD));
        searchMemory.push(mbr);
    }
}

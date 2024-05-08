package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.cache.component.Coord;
import org.riseger.main.system.cache.component.Coords;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Coords_fc extends Function_c {
    public Coords_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Coords coords = (Coords) searchMemory.poll();
        Coord tmp = (Coord) searchMemory.poll();
        coords.add(tmp);
        searchMemory.push(coords);
    }
}

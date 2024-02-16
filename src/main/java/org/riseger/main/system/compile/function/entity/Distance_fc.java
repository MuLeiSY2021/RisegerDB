package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.cache.component.Coord_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Distance_fc extends Function_c {

    public Distance_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Coord_c coordinates2 = (Coord_c) searchMemory.poll(),
                coordinates1 = (Coord_c) searchMemory.poll();
        Double distance = coordinates1.distance(coordinates2);
        searchMemory.push(distance);
    }
}

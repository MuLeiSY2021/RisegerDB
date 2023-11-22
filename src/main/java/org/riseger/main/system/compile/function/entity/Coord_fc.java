package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.cache.entity.component.Coord_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Coord_fc extends Function_c {

    public Coord_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Coord_c result = new Coord_c((Double) searchMemory.poll(), (Double) searchMemory.poll());
        searchMemory.push(result);
    }
}

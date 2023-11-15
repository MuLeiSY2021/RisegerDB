package org.riseger.main.compiler.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Coord_fc extends Function_c {

    public Coord_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Coord_c result = new Coord_c((Double) poll(), (Double) poll());
        super.put(result);
    }
}

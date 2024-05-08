package org.riseger.main.system.compile.function.number;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class NegivateNumber_fc extends Function_c {

    public NegivateNumber_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Number x1 = (Number) searchMemory.poll();
        searchMemory.push(-((Double) x1));
    }
}

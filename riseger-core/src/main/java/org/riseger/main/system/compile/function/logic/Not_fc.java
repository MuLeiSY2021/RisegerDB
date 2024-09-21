package org.riseger.main.system.compile.function.logic;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Not_fc extends Function_c {

    public Not_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        boolean f1 = (Boolean) searchMemory.poll();
        boolean result = !f1;
        searchMemory.push(result);
    }

}

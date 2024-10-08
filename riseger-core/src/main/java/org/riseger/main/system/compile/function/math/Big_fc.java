package org.riseger.main.system.compile.function.math;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Big_fc extends Function_c {

    public Big_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Number x2 = (Number) searchMemory.poll(),
                x1 = (Number) searchMemory.poll();

        boolean result = ((Comparable) x1).compareTo(x2) > 0;
        LOG.debug(x1 + " > " + x2 + " " + result);
        searchMemory.push(result);
    }
}

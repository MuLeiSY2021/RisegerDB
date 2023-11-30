package org.riseger.main.system.compile.function.math;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Equal_fc extends Function_c {
    public Equal_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Number x1 = (Number) searchMemory.poll(),
                x2 = (Number) searchMemory.poll();

        boolean result = ((Comparable) x1).compareTo(x2) == 0;
        searchMemory.push(result);
    }

}

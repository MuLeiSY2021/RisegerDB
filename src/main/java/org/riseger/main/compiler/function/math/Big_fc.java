package org.riseger.main.compiler.function.math;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Big_fc extends Function_c {

    public Big_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Number x2 = (Number) super.poll(),
                x1 = (Number) super.poll();

        boolean result = ((Comparable) x1).compareTo(x2) > 0;
        LOG.debug(x1 + " > " + x2 + " " + result);
        super.put(result);
    }
}

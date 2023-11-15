package org.riseger.main.compiler.function.math;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Equal_fc extends Function_c {
    public Equal_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Number x1 = (Number) super.poll(),
                x2 = (Number) super.poll();

        boolean result = ((Comparable) x1).compareTo(x2) == 0;
        super.put(result);
    }

}

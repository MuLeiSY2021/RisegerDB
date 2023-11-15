package org.riseger.main.compiler.function.number;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class NegivateNumber_fc extends Function_c {

    public NegivateNumber_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Number x1 = (Number) super.poll();
        super.put(-((Double) x1));
    }
}

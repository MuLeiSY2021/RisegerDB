package org.riseger.main.compiler.function.number;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class MutiNumber_fc extends Function_c {

    public MutiNumber_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Double x1 = (Double) super.poll(),
                x2 = (Double) super.poll();
        super.put(x1 * x2);
    }
}

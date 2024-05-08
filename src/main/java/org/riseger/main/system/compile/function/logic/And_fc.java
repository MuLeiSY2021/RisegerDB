package org.riseger.main.system.compile.function.logic;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.ProcessorFunction;
import org.riseger.protocol.exception.SQLException;

import java.util.List;

public class And_fc extends Function_c implements ProcessorFunction {

    public And_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        boolean f1 = (Boolean) searchMemory.poll(),
                f2 = (Boolean) searchMemory.poll();
        boolean result = f1 & f2;
        LOG.debug(f1 + " & " + f2 + " " + result);
        searchMemory.push(result);
    }

    @Override
    public List<Function_f> preProcess() {
        return null;
    }
}

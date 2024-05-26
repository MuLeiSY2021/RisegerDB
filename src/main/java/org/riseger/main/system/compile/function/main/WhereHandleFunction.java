package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.protocol.exception.SQLException;

public interface WhereHandleFunction {
    void postProcess(SearchMemory searchMemory, CommandList commandList) throws SQLException;
}

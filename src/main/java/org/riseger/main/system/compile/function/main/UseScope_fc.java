package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class UseScope_fc extends Function_c {

    public UseScope_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        searchMemory.setMap(searchMemory.poll(), MemoryConstant.SCOPE);
    }

}

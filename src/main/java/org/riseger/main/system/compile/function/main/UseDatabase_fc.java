package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.CacheSystem;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class UseDatabase_fc extends Function_c {


    public UseDatabase_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        searchMemory.setMap(CacheSystem.INSTANCE.getDatabase((String) searchMemory.poll()), MemoryConstant.DATABASE);
    }
}

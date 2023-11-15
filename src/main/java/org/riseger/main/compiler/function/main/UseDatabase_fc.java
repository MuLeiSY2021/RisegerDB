package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class UseDatabase_fc extends Function_c {


    public UseDatabase_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        setMap(CacheMaster.INSTANCE.getDatabase((String) poll()), MemoryConstant.DATABASE);
    }
}

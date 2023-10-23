package org.riseger.main.sql.function.main;

import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class UseDatabase_fc extends MainFunction_c {


    public UseDatabase_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        setMap(CacheMaster.INSTANCE.getDatabase((String) poll()), MemoryConstant.DATABASE);
    }
}

package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class UseDatabase_fc extends MainFunction_c {


    public UseDatabase_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        setMap(CacheMaster.INSTANCE.getDatabase((String) poll()), MemoryConstant.DATABASE);
    }
}

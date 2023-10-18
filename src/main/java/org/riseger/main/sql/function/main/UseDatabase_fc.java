package org.riseger.main.sql.function.main;

import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.MemoryConstant;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.main.USE_DATABASE;

public class UseDatabase_fc extends MainFunction_c {
    private String name;


    public UseDatabase_fc(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        super(function, memory, threshold, commandList);
        USE_DATABASE use_database = (USE_DATABASE) function;
        this.name = use_database.getName();
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        super.setMap(CacheMaster.INSTANCE.getDatabase(name), MemoryConstant.DATABASE);
    }
}

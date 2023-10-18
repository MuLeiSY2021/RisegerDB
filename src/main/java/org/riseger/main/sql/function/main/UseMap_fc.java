package org.riseger.main.sql.function.main;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.MemoryConstant;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.main.USE_MAP;

public class UseMap_fc extends MainFunction_c {
    private String name;

    public UseMap_fc(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        super(function, memory, threshold, commandList);
        USE_MAP use_map = (USE_MAP) function;
        this.name = use_map.getName();
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Database_c db = (Database_c) super.getMap(MemoryConstant.DATABASE);
        super.setMap(db.getMap(name), MemoryConstant.DATABASE);
    }

}

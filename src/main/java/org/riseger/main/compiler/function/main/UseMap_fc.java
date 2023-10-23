package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class UseMap_fc extends MainFunction_c {

    public UseMap_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Database_c db = (Database_c) super.getMap(MemoryConstant.DATABASE);
        super.setMap(db.getMap((String) poll()), MemoryConstant.DATABASE);
    }

}

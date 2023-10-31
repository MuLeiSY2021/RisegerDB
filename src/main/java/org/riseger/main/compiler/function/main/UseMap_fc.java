package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
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
        MapDB_c map = db.getMap((String) poll());
        super.setMap(map, MemoryConstant.DATABASE);
        super.setMap(map.getThreshold(),MemoryConstant.THRESHOLD);
    }

}

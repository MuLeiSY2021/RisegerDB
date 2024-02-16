package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.MBRectangle_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class PostWhere_fc extends Function_c {

    public PostWhere_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        WhereIterator iterator = (WhereIterator) searchMemory.get(MemoryConstant.WHERE);
        MBRectangle_c mbRectangle = iterator.next();
        searchMemory.setMap(mbRectangle, MemoryConstant.ELEMENT);
    }

}

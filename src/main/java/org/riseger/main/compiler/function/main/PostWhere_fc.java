package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class PostWhere_fc extends Function_c {

    public PostWhere_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        WhereIterator iterator = (WhereIterator) get(MemoryConstant.WHERE);
        MBRectangle_c mbRectangle = iterator.next();
        super.setMap(mbRectangle, MemoryConstant.ELEMENT);
    }

}

package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class PostWhere_fc extends MainFunction_c {

    public PostWhere_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        WhereIterator iterator = (WhereIterator) getMap(MemoryConstant.WHERE);
        MBRectangle_c mbRectangle = iterator.next();
        super.setMap(mbRectangle, MemoryConstant.ELEMENT);
    }

}

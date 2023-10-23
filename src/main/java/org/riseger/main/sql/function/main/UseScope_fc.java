package org.riseger.main.sql.function.main;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.MemoryConstant;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class UseScope_fc extends MainFunction_c {

    public UseScope_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        super.setMap(super.poll(), MemoryConstant.SCOPE);
    }

}
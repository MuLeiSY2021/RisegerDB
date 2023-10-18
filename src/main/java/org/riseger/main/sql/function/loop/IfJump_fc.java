package org.riseger.main.sql.function.loop;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.LoopFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.function.FUNCTION;

public class IfJump_fc extends LoopFunction_c {
    public IfJump_fc(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        super(function, memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        boolean result = (boolean) super.poll();
        if (result) {
            super.jumpTo((Integer) super.poll());
        } else {
            super.put(super.index());
        }
    }

}

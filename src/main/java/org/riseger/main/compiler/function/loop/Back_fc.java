package org.riseger.main.compiler.function.loop;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.LoopFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Back_fc extends LoopFunction_c {
    public Back_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        int index = (int) super.poll();
        super.jumpTo(index);
    }
}

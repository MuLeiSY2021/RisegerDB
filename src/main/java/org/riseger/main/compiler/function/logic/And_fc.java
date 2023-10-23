package org.riseger.main.compiler.function.logic;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.BooleanFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class And_fc extends BooleanFunction_c {

    public And_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        boolean f1 = (Boolean) super.poll(),
                f2 = (Boolean) super.poll();
        boolean result = f1 & f2;
        super.put(result);
    }
}

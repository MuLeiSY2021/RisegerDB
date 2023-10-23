package org.riseger.main.compiler.function.logic;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.BooleanFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Not_fc extends BooleanFunction_c {

    public Not_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        boolean f1 = (Boolean) super.poll();
        boolean result = !f1;
        super.put(result);
    }

}

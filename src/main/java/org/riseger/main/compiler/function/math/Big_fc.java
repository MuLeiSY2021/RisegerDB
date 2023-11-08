package org.riseger.main.compiler.function.math;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.BooleanFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Big_fc extends BooleanFunction_c {

    public Big_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Number x2 = (Number) super.poll(),
                x1 = (Number) super.poll();

        boolean result = ((Comparable) x1).compareTo(x2) > 0;
        LOG.debug(x1 + " > " + x2 + " " + result);
        super.put(result);
    }
}

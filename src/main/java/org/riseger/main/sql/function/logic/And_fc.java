package org.riseger.main.sql.function.logic;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class And_fc extends BooleanFunction_c {

    public And_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        boolean f1 = (Boolean) super.poll(),
                f2 = (Boolean) super.poll();
        boolean result = f1 & f2;
        super.put(result);
    }
}

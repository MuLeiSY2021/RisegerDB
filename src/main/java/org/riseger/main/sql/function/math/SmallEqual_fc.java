package org.riseger.main.sql.function.math;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class SmallEqual_fc extends BooleanFunction_c {
    public Big_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Number x1 = (Number) super.poll(),
                x2 = (Number) super.poll();

        boolean result = ((Comparable) x1).compareTo(x2) <= 0;
        super.put(result);
    }
}

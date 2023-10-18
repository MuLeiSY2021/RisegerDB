package org.riseger.main.sql.function.type;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public abstract class BooleanFunction_c extends Function_c {
    public BooleanFunction_c(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        super(function, memory, threshold, commandList);
    }
}

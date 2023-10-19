package org.riseger.main.sql.function.type;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;

public abstract class NumberFunction_c extends Function_c {
    public NumberFunction_c(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

}

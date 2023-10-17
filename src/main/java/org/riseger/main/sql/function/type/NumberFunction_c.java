package org.riseger.main.sql.function.type;

import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public abstract class NumberFunction_c extends Function_c {
    public NumberFunction_c(FUNCTION function, SearchMemory memory, double threshold) {
        super(function, memory, threshold);
    }
}

package org.riseger.main.sql.function.type;

import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public abstract class CoordFunction_c extends Function_c {
    public CoordFunction_c(FUNCTION function, SearchMemory memory, double threshold) {
        super(function, memory, threshold);
    }
}

package org.riseger.main.sql.function.type;

import org.riseger.main.sql.search.SearchMemory;

public abstract class BooleanFunction_c extends Function_c<Boolean> {
    public BooleanFunction_c(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }
}

package org.riseger.main.search.function.type;

import org.riseger.main.search.SearchMemory;

public abstract class BooleanFunction_c extends Function_c<Boolean> {
    public BooleanFunction_c(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }
}

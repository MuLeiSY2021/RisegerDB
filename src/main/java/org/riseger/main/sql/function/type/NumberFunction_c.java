package org.riseger.main.sql.function.type;

import org.riseger.main.sql.search.SearchMemory;

public abstract class NumberFunction_c extends Function_c<Number> {
    public NumberFunction_c(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }
}

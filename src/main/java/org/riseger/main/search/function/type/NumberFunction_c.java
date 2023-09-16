package org.riseger.main.search.function.type;

import org.riseger.main.search.SearchMemory;

public abstract class NumberFunction_c extends Function_c<Number> {
    public NumberFunction_c(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }
}

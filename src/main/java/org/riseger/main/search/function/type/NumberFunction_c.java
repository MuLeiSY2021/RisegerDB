package org.riseger.main.search.function.type;

import org.riseger.main.search.SearchMemory;

public abstract class NumberFunction_c extends Function_c<Number>{
    public NumberFunction_c(int indexStart, SearchMemory memory, double threshold) {
        super(indexStart, memory, threshold);
    }
}

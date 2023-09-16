package org.riseger.main.search.function.type;


import org.riseger.main.search.SearchMemory;

public abstract class UniversalFunction_c extends Function_c<Object> {
    public UniversalFunction_c(int indexStart, SearchMemory memory, double threshold) {
        super(indexStart, memory, threshold);
    }
}

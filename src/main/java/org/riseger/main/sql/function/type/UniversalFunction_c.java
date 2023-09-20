package org.riseger.main.sql.function.type;


import org.riseger.main.sql.search.SearchMemory;

public abstract class UniversalFunction_c extends Function_c<Object> {
    public UniversalFunction_c(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }
}

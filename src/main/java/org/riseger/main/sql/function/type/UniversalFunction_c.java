package org.riseger.main.sql.function.type;


import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public abstract class UniversalFunction_c extends Function_c {
    public UniversalFunction_c(FUNCTION function, SearchMemory memory, double threshold) {
        super(function, memory, threshold);
    }
}

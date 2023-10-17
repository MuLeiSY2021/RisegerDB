package org.riseger.main.sql.function.entity;

import org.riseger.main.sql.function.type.NumberFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class Distance_fc extends NumberFunction_c {

    public Distance_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

}

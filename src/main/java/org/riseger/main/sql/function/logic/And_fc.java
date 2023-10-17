package org.riseger.main.sql.function.logic;

import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class And_fc extends BooleanFunction_c {
    boolean f1;

    boolean f2;

    public And_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

}

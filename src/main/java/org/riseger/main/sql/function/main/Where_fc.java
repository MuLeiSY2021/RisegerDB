package org.riseger.main.sql.function.main;

import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class Where_fc extends MainFunction_c {

    public Where_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);

    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

}

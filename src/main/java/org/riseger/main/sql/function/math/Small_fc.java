package org.riseger.main.sql.function.math;

import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;

public class Small_fc extends BooleanFunction_c {
    Number x1;

    Number x2;

    public Small_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {
        //这个函数多少有点多余了........
    }

}

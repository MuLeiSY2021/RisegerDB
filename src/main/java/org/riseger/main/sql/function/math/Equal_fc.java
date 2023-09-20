package org.riseger.main.sql.function.math;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class Equal_fc extends BooleanFunction_c {
    Number x1;

    Number x2;

    public Equal_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {
        //这个函数多少有点多余了........
    }

    @Override
    public Boolean resolve(Element_c element) {
        x1 = (Number) super.get();
        x2 = (Number) super.get();
        boolean result = ((Comparable) x1).compareTo(x2) == 0;
        super.set(result);
        return result;
    }
}

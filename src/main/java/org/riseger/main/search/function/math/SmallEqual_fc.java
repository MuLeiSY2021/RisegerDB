package org.riseger.main.search.function.math;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.BooleanFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;

public class SmallEqual_fc extends BooleanFunction_c {
    Number x1;

    Number x2;

    public SmallEqual_fc(int indexStart, SearchMemory memory, double threshold) {
        super(indexStart, memory, threshold);
    }
    @Override
    public void setFunction(FUNCTION condition) {
        //这个函数多少有点多余了........
    }

    @Override
    public Boolean resolve(Element_c element) {
        x1 = (Number) super.get(1);
        x2 = (Number) super.get(2);
        boolean result = ((Comparable)x1).compareTo(x2) <= 0;
        super.set(result);
        return result;
    }
}

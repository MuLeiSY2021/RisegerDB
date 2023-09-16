package org.riseger.main.search.function.logic;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.BooleanFunction_c;
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

    @Override
    public Boolean resolve(Element_c element) {
        f1 = (Boolean) super.get();
        f2 = (Boolean) super.get();
        boolean result = f1 & f2;
        super.set(result);
        return result;
    }
}

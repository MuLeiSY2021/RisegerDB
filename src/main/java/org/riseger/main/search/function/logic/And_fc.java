package org.riseger.main.search.function.logic;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.BooleanFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;

public class And_fc extends BooleanFunction_c {
    boolean f1;

    boolean f2;

    public And_fc(int indexStart, SearchMemory memory) {
        super(indexStart, memory);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public Boolean getResult(Element_c element) {
        f1 = (Boolean) super.get(1);
        f2 = (Boolean) super.get(2);
        boolean result = f1 & f2;
        super.set(result);
        return result;
    }
}

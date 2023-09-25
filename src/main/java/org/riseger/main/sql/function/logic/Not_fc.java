package org.riseger.main.sql.function.logic;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class Not_fc extends BooleanFunction_c {
    boolean f1;

    public Not_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public Boolean resolve(Element_c element) {
        f1 = (Boolean) super.get();
        boolean result = !f1;
        super.set(result);
        return result;
    }
}
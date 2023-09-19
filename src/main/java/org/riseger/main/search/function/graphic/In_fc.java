package org.riseger.main.search.function.graphic;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.BooleanFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;

public class In_fc extends BooleanFunction_c {
    MBRectangle_c r;

    public In_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public Boolean resolve(Element_c element) {
        r = (MBRectangle_c) super.get();
        boolean result = r.inner(element);
        super.set(result);
        return result;
    }
}
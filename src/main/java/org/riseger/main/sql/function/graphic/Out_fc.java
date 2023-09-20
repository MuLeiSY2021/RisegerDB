package org.riseger.main.sql.function.graphic;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class Out_fc extends BooleanFunction_c {
    MBRectangle_c r;

    public Out_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public Boolean resolve(Element_c element) {
        r = (MBRectangle_c) super.get();
        boolean result = !r.intersects(element);
        super.set(result);
        return result;
    }
}

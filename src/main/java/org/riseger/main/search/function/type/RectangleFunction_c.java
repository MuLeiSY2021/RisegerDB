package org.riseger.main.search.function.type;

import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.search.SearchMemory;

public abstract class RectangleFunction_c extends Function_c<MBRectangle_c> {

    public RectangleFunction_c(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }
}

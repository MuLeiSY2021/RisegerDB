package org.riseger.main.search.function.type;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.search.SearchMemory;

public abstract class CoordFunction_c extends Function_c<Coord_c> {
    public CoordFunction_c(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }
}

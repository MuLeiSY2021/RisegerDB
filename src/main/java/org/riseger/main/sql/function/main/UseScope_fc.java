package org.riseger.main.sql.function.main;

import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.main.sql.search.SearchMemory;

public class UseScope_fc extends MainFunction_c {
    public MBRectangle_c rectangle;

    public UseScope_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }


}

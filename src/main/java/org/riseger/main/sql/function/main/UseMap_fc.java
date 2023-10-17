package org.riseger.main.sql.function.main;

import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.main.USE_MAP;

public class UseMap_fc extends MainFunction_c {
    private String name;

    public UseMap_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);

    }

    @Override
    public void setFunction(FUNCTION condition) {
        USE_MAP use_database = (USE_MAP) condition;
        this.name = use_database.getName();
    }

}

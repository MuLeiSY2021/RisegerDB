package org.riseger.main.sql.function.main;

import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.main.USE_DATABASE;

public class UseDatabase_fc extends MainFunction_c {
    private String name;

    public UseDatabase_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);

    }

    @Override
    public void setFunction(FUNCTION condition) {
        USE_DATABASE use_database = (USE_DATABASE) condition;
        this.name = use_database.getName();
    }

}

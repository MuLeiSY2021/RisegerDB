package org.riseger.main.sql.function.main;

import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.main.USE_MODEL;

import java.util.List;

public class UseModel_fc extends MainFunction_c {
    private List<String> models;

    public UseModel_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {
        USE_MODEL use_database = (USE_MODEL) condition;
        this.models = use_database.getModels();
    }

}

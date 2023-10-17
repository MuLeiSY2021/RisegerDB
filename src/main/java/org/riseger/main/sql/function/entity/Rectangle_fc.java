package org.riseger.main.sql.function.entity;

import org.riseger.main.sql.function.type.RectangleFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class Rectangle_fc extends RectangleFunction_c {

    public Rectangle_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

}

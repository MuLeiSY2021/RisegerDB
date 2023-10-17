package org.riseger.main.sql.function.entity;

import org.apache.log4j.Logger;
import org.riseger.main.sql.function.type.UniversalFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.basic.ATTRIBUTE;

public class Attribute_fc extends UniversalFunction_c {
    private static final Logger LOG = Logger.getLogger(Attribute_fc.class);

    String model;

    String attribute;

    public Attribute_fc(FUNCTION function, SearchMemory memory, double threshold) {
        super(function, memory, threshold);
        this.attribute = ((ATTRIBUTE) function).getAttribute();
        this.model = ((ATTRIBUTE) function).getModel();
    }

}

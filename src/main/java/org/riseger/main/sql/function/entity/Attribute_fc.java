package org.riseger.main.sql.function.entity;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.MemoryConstant;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.UniversalFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.Attribute_F;

public class Attribute_fc extends UniversalFunction_c {
    private static final Logger LOG = Logger.getLogger(Attribute_fc.class);

    String model;

    String attribute;

    public Attribute_fc(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        super(function, memory, threshold, commandList);
        this.attribute = ((Attribute_F) function).getAttribute();
        this.model = ((Attribute_F) function).getModel();
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Element_c element = (Element_c) super.getMap(MemoryConstant.ELEMENT);
        if (element.getModel().equals(model)) {
            Object result = element.getAttribute(attribute);
            if (result == null) {
                throw new IllegalSearchAttributeException(attribute);
            }
            super.put(result);
        } else {
            LOG.error(this.model + " element:" + element.getModel());
            throw new IllegalSearchAttributeException("Not such model");
        }
    }

}

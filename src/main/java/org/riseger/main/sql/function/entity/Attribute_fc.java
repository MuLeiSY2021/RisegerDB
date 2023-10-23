package org.riseger.main.sql.function.entity;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.sql.function.type.UniversalFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Attribute_fc extends UniversalFunction_c {
    private static final Logger LOG = Logger.getLogger(Attribute_fc.class);

    public Attribute_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        String model = (String) poll(), attribute = (String) poll();
        Element_c element = (Element_c) super.getMap(MemoryConstant.ELEMENT);
        if (element.getModel().equals(model)) {
            Object result = element.getAttribute(attribute);
            if (result == null) {
                throw new IllegalSearchAttributeException(attribute);
            }
            super.put(result);
        } else {
            LOG.error(model + " element:" + element.getModel());
            throw new IllegalSearchAttributeException("Not such model");
        }
    }

}

package org.riseger.main.compiler.function.entity;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.UniversalFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.serializer.JsonSerializer;

public class Attribute_fc extends UniversalFunction_c {
    private static final Logger LOG = Logger.getLogger(Attribute_fc.class);

    public Attribute_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        String attribute = (String) poll(), model = (String) poll();
        Element_c element = (Element_c) super.getMap(MemoryConstant.ELEMENT);
        if (element.getModel().equals(model)) {
            Object result = element.getAttribute(attribute);
            if (result == null) {
                throw new IllegalSearchAttributeException(attribute);
            }
            super.put(result);
        } else {
            IllegalSearchAttributeException e = new IllegalSearchAttributeException("Element:" + JsonSerializer.serializeToString(element) + "Not have This attribute:" + model + "." + attribute);
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

}

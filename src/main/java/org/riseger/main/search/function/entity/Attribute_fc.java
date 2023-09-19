package org.riseger.main.search.function.entity;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.UniversalFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.basic.ATTRIBUTE;

public class Attribute_fc extends UniversalFunction_c {
    private static final Logger LOG = Logger.getLogger(Attribute_fc.class);

    String model;

    String attribute;

    public Attribute_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {
        this.attribute = ((ATTRIBUTE) condition).getAttribute();
        this.model = ((ATTRIBUTE) condition).getModel();
    }

    @Override
    public Object resolve(Element_c element) throws IllegalSearchAttributeException {
        if (element.getModel().equals(model)) {
            Object result = element.getAttribute(attribute);
            if (result == null) {
                throw new IllegalSearchAttributeException(attribute);
            }
            super.set(result);
            return result;
        } else {
            LOG.error(this.model + " element:" + element.getModel());
            throw new IllegalSearchAttributeException("Not such model");

        }
    }
}

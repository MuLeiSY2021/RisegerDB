package org.riseger.main.search.function.entity;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.UniversalFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.basic.ATTRIBUTE;

public class Attribute_fc extends UniversalFunction_c {
    String attribute;

    public Attribute_fc(int indexStart, SearchMemory memory, double threshold) {
        super(indexStart, memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {
        this.attribute = ((ATTRIBUTE) condition).getAttribute();
    }

    @Override
    public Object resolve(Element_c element) {
        Object result = element.getAttribute(attribute);
        super.set(result);
        return result;
    }
}

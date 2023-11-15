package org.riseger.main.compiler.function.entity;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.serializer.JsonSerializer;

public class Attribute_fc extends Function_c {
    private static final Logger LOG = Logger.getLogger(Attribute_fc.class);

    public Attribute_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        String attribute = (String) poll(), model = (String) poll();
        Element_c element = (Element_c) super.getMap(MemoryConstant.ELEMENT);
        if (element.getModel().equals(model)) {
            Object result = element.getAttribute(attribute);
            if (result == null) {
                throw new SQLException(attribute);
            }
            super.put(result);
        } else {
            SQLException e = new SQLException("Element:" + JsonSerializer.serializeToString(element) + "Not have This attribute:" + model + "." + attribute);
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

}

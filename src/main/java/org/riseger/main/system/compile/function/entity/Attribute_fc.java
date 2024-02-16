package org.riseger.main.system.compile.function.entity;

import org.apache.log4j.Logger;
import org.riseger.main.system.cache.component.Element_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.serializer.JsonSerializer;

public class Attribute_fc extends Function_c {
    private static final Logger LOG = Logger.getLogger(Attribute_fc.class);

    public Attribute_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        String attribute = (String) searchMemory.poll(), model = (String) searchMemory.poll();
        Element_c element = (Element_c) searchMemory.get(MemoryConstant.ELEMENT);
        if (element.getModel().equals(model)) {
            Object result = element.getAttribute(attribute);
            if (result == null) {
                throw new SQLException(attribute);
            }
            searchMemory.push(result);
        } else {
            SQLException e = new SQLException("Element:" + JsonSerializer.serializeToString(element) + "Not have This attribute:" + model + "." + attribute);
            LOG.error(e.getMessage(), e);
            throw e;
        }
    }

}

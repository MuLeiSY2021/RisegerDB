package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.Element;
import org.riseger.main.system.compile.clazz.DotString;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Update_fc extends Function_c {
    public Update_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        DotString attributes = (DotString) searchMemory.poll();
        Object value = searchMemory.poll();
        if (attributes.length() != 1) {
            SQLException e = new SQLException("Attributes:Not illegal:" + attributes);
            LOG.error(e.getMessage(), e);
            throw e;
        }
        String attribute = attributes.getBottom();
        Element element = (Element) searchMemory.get(MemoryConstant.ELEMENT);
        try {
            element.setAttribute(attribute, value);
        } catch (Exception e) {
            SQLException er = new SQLException("Attributes:Not illegal:" + attributes);
            LOG.error(er.getMessage(), er);
            throw er;
        }
    }
}

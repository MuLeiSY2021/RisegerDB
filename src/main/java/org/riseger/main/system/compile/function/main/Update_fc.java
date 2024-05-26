package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.Element;
import org.riseger.main.system.compile.clazz.DotString;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Update_fc extends Function_c implements WhereHandleFunction {
    public Update_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Object[] args = new Object[2];
        args[0] = searchMemory.poll();
        args[1] = searchMemory.poll();
        searchMemory.setMap(args, MemoryConstant.METOD_PARAMATER);
        searchMemory.setMap(this, MemoryConstant.METOD_PROCESS);
    }

    @Override
    public void postProcess(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Object[] args = (Object[]) searchMemory.get(MemoryConstant.METOD_PARAMATER);
        DotString attributes = (DotString) args[1];
        Object value = args[0];
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

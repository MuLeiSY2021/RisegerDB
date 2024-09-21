package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.compile.clazz.DotString;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class TopDotStrings_fc extends Function_c {
    public TopDotStrings_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        DotString tmp = new DotString((String) searchMemory.poll());
        searchMemory.push(tmp);
    }
}
package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.compile.clazz.DotString;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class DotStrings_fc extends Function_c {
    public DotStrings_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        DotString strings = (DotString) searchMemory.poll();
        String tmp = (String) searchMemory.poll();
        strings.add(tmp);
        searchMemory.push(strings);
    }
}
package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.List;

public class Strings_fc extends Function_c {
    public Strings_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        List<String> strings = (List<String>) searchMemory.poll();
        String tmp = (String) searchMemory.poll();
        strings.add(tmp);
        searchMemory.push(strings);
    }
}

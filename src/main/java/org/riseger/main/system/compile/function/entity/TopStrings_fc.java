package org.riseger.main.system.compile.function.entity;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.LinkedList;
import java.util.List;

public class TopStrings_fc extends Function_c {
    public TopStrings_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        String tmp = (String) searchMemory.poll();
        List<String> strings = new LinkedList<>();
        strings.add(tmp);
        searchMemory.push(strings);
    }
}
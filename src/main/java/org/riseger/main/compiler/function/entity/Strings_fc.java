package org.riseger.main.compiler.function.entity;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.List;

public class Strings_fc extends Function_c {
    public Strings_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        List<String> strings = (List<String>) super.poll();
        String tmp = (String) super.poll();
        strings.add(tmp);
        super.put(strings);
    }
}

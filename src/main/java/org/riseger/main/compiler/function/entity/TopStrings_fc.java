package org.riseger.main.compiler.function.entity;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.LinkedList;
import java.util.List;

public class TopStrings_fc extends Function_c {
    public TopStrings_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        String tmp = (String) super.poll();
        List<String> strings = new LinkedList<>();
        strings.add(tmp);
        super.put(strings);
    }
}
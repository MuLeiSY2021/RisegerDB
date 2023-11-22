package org.riseger.main.system.compile.function.number;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;

public class SubNumber_fc extends Function_c {

    public SubNumber_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) {
        Double x1 = (Double) searchMemory.poll(),
                x2 = (Double) searchMemory.poll();
        searchMemory.push(x1 - x2);
    }
}


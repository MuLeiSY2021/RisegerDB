package org.riseger.main.sql.function.main;

import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.MainFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.main.SEARCH;

public class Search_fc extends MainFunction_c {
    public Search_fc(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        super(function, memory, threshold, commandList);
        SEARCH search = (SEARCH) function;
    }
}

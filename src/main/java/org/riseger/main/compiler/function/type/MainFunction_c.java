package org.riseger.main.compiler.function.type;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;

public abstract class MainFunction_c extends Function_c {
    public MainFunction_c(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }
}
package org.riseger.protoctl.search.command;

import lombok.Getter;
import org.riseger.protoctl.search.function.type.BOOL_functional;

@Getter
public class WHERE extends SQL {
    private BOOL_functional condition;

    public void condition(BOOL_functional function) {
        this.condition = function;
    }
}

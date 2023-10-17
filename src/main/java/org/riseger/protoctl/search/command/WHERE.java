package org.riseger.protoctl.search.command;

import lombok.Getter;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONAL;

@Getter
public class WHERE extends SQL {
    private BOOL_FUNCTIONAL condition;

    public void condition(BOOL_FUNCTIONAL function) {
        this.condition = function;
    }
}

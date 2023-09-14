package org.riseger.protoctl.search.command;

import lombok.Getter;
import org.riseger.protoctl.search.function.type.BOOL_FUNCTIONBLE;

@Getter
public class WHERE extends SQL {
    private BOOL_FUNCTIONBLE condition;

    public WHERE condition(BOOL_FUNCTIONBLE function) {
        this.condition = function;
        return this;
    }
}

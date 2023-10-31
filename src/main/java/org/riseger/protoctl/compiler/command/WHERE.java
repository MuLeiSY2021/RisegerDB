package org.riseger.protoctl.compiler.command;

import lombok.Getter;
import org.riseger.protoctl.compiler.function.type.BOOL_functional;

@Getter
public class WHERE extends SQL {
    private BOOL_functional condition;

    public void condition(BOOL_functional function) {
        this.condition = function;
    }
}

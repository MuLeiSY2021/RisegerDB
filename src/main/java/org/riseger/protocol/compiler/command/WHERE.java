package org.riseger.protocol.compiler.command;

import lombok.Getter;
import org.riseger.protocol.compiler.function.type.BOOL_functional;

@Getter
public class WHERE extends SQL {
    private BOOL_functional condition;

    public void condition(BOOL_functional function) {
        this.condition = function;
    }
}

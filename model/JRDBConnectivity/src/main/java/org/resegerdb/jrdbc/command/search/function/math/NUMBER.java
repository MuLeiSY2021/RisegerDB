package org.resegerdb.jrdbc.command.search.function.math;

import org.resegerdb.jrdbc.command.search.function.type.NUMBER_FUNCTION;

public class NUMBER extends NUMBER_FUNCTION {
    private Number number;

    public NUMBER_FUNCTION invoke(Number number) {
        this.number = number;
        return this;
    }
}

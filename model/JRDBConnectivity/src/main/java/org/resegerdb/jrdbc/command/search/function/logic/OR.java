package org.resegerdb.jrdbc.command.search.function.logic;

import org.resegerdb.jrdbc.command.search.function.type.BOOL_FUNCTION;

public class OR implements BOOL_FUNCTION {
    private BOOL_FUNCTION function1;

    private BOOL_FUNCTION function2;

    public BOOL_FUNCTION invoke(BOOL_FUNCTION function1, BOOL_FUNCTION function2) {
        this.function1 = function1;
        this.function2 = function2;
        return this;
    }
}

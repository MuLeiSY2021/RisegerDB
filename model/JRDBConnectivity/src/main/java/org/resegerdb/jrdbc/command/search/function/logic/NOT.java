package org.resegerdb.jrdbc.command.search.function.logic;

import org.resegerdb.jrdbc.command.search.function.type.BOOL_FUNCTION;

public class NOT implements BOOL_FUNCTION {
    private BOOL_FUNCTION function;

    public BOOL_FUNCTION invoke(BOOL_FUNCTION function) {
        this.function = function;
        return this;
    }
}

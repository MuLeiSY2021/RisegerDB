package org.resegerdb.jrdbc.command.search.function.graphic;

import org.resegerdb.jrdbc.command.search.function.type.BOOL_FUNCTION;
import org.resegerdb.jrdbc.command.search.function.type.RECTANGLE_FUNCTION;

public class OUT implements BOOL_FUNCTION {

    private RECTANGLE_FUNCTION rect;

    public BOOL_FUNCTION invoke(RECTANGLE_FUNCTION rect) {
        this.rect = rect;
        return this;
    }
}

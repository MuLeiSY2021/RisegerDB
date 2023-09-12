package org.resegerdb.jrdbc.command.search.function.math;

import org.resegerdb.jrdbc.command.search.function.type.BOOL_FUNCTION;
import org.resegerdb.jrdbc.command.search.function.type.NUMBER_FUNCTION;

public class SMALL_EQUAL implements BOOL_FUNCTION {
    private NUMBER_FUNCTION number1;

    private NUMBER_FUNCTION number2;

    public BOOL_FUNCTION invoke(NUMBER_FUNCTION number1, NUMBER_FUNCTION number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }
}
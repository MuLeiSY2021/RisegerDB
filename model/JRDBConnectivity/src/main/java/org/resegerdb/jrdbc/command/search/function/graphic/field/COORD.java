package org.resegerdb.jrdbc.command.search.function.graphic.field;

import org.resegerdb.jrdbc.command.search.function.type.COORD_FUNCTION;

public class COORD extends COORD_FUNCTION {
    double number1;

    double number2;

    public COORD_FUNCTION invoke(double number1, double number2) {
        this.number1 = number1;
        this.number2 = number2;
        return this;
    }
}

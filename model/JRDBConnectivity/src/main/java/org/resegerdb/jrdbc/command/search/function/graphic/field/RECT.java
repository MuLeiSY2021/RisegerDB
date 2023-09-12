package org.resegerdb.jrdbc.command.search.function.graphic.field;

import org.resegerdb.jrdbc.command.search.function.type.COORD_FUNCTION;
import org.resegerdb.jrdbc.command.search.function.type.NUMBER_FUNCTION;
import org.resegerdb.jrdbc.command.search.function.type.RECTANGLE_FUNCTION;

public class RECT extends RECTANGLE_FUNCTION {

    private COORD_FUNCTION coord;

    private NUMBER_FUNCTION len;

    public RECTANGLE_FUNCTION invoke(COORD_FUNCTION coord, NUMBER_FUNCTION len) {
        //TODO:注意这里要标注为不同的函数定义数
        this.coord = coord;
        this.len = len;
        return this;
    }
}

package org.resegerdb.jrdbc.command.search.function.graphic;

import org.resegerdb.jrdbc.command.search.function.type.COORD_FUNCTION;
import org.resegerdb.jrdbc.command.search.function.type.NUMBER_FUNCTION;

public class DISTANCE extends NUMBER_FUNCTION {
    private COORD_FUNCTION coord1;

    private COORD_FUNCTION coord2;

    public NUMBER_FUNCTION invoke(COORD_FUNCTION coord1, COORD_FUNCTION coord2) {
        //TODO:注意这里要标注为不同的函数定义数
        this.coord1 = coord1;
        this.coord2 = coord2;
        return this;
    }


}

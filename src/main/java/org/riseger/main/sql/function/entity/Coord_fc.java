package org.riseger.main.sql.function.entity;

import org.riseger.main.sql.function.type.CoordFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.field.COORD;

public class Coord_fc extends CoordFunction_c {
    Double[] n;

    public Coord_fc(FUNCTION function, SearchMemory memory, double threshold) {
        super(function, memory, threshold);
        COORD nbmf = (COORD) function;
        this.n = new Double[2];
        this.n[0] = nbmf.getNumber1();
        this.n[1] = nbmf.getNumber2();
    }
}

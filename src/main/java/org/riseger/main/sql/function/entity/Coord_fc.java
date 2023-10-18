package org.riseger.main.sql.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.CoordFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.COORD;

public class Coord_fc extends CoordFunction_c {

    Double[] n;

    public Coord_fc(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        super(function, memory, threshold, commandList);
        COORD nbmf = (COORD) function;
        this.n = new Double[2];
        this.n[0] = nbmf.getNumber1();
        this.n[1] = nbmf.getNumber2();
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Coord_c result = new Coord_c(n[0], n[1]);
        super.put(result);
    }
}

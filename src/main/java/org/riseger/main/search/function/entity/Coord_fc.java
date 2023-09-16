package org.riseger.main.search.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.CoordFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.entity.field.COORD;

public class Coord_fc extends CoordFunction_c {
    Double[] n;

    public Coord_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {
        COORD nbmf = (COORD) condition;
        this.n = new Double[2];
        this.n[0] = nbmf.getNumber1();
        this.n[1] = nbmf.getNumber2();
    }

    @Override
    public Coord_c resolve(Element_c element) {
        Coord_c result = new Coord_c(n[0], n[1]);
        super.set(result);
        return result;
    }
}

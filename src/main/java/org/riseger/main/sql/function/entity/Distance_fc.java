package org.riseger.main.sql.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.sql.function.type.NumberFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class Distance_fc extends NumberFunction_c {

    public Distance_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public Number resolve(Element_c element) {
        Coord_c coordinates2 = (Coord_c) super.get(),
                coordinates1 = (Coord_c) super.get();
        Double distance = coordinates1.distance(coordinates2);
        super.set(distance);
        return distance;
    }
}

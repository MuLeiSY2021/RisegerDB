package org.riseger.main.sql.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.NumberFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Distance_fc extends NumberFunction_c {

    public Distance_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Coord_c coordinates2 = (Coord_c) super.poll(),
                coordinates1 = (Coord_c) super.poll();
        Double distance = coordinates1.distance(coordinates2);
        super.put(distance);
    }
}

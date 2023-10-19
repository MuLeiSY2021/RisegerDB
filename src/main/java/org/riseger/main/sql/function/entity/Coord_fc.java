package org.riseger.main.sql.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.CoordFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Coord_fc extends CoordFunction_c {

    public Coord_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Coord_c result = new Coord_c((Double) poll(), (Double) poll());
        super.put(result);
    }
}

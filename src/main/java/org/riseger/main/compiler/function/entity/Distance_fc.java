package org.riseger.main.compiler.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class Distance_fc extends Function_c {

    public Distance_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Coord_c coordinates2 = (Coord_c) super.poll(),
                coordinates1 = (Coord_c) super.poll();
        Double distance = coordinates1.distance(coordinates2);
        super.put(distance);
    }
}

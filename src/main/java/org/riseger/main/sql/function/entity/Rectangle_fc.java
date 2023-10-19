package org.riseger.main.sql.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.RectangleFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Rectangle_fc extends RectangleFunction_c {

    public Rectangle_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Double len = ((Number) super.poll()).doubleValue();
        Coord_c coord = (Coord_c) super.poll();
        MBRectangle_c mbr = new MBRectangle_c(coord, len, super.getThreshold());
        super.put(mbr);
    }
}

package org.riseger.main.search.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.RectangleFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;

public class Rectangle_fc extends RectangleFunction_c {

    public Rectangle_fc(SearchMemory memory, double threshold) {
        super(memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public MBRectangle_c resolve(Element_c element) {
        Double len = ((Number) super.get()).doubleValue();
        Coord_c coord = (Coord_c) super.get();
        MBRectangle_c mbr = new MBRectangle_c(coord, len, super.getThreshold());
        super.set(mbr);
        return mbr;
    }
}

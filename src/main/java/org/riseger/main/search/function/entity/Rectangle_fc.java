package org.riseger.main.search.function.entity;

import org.riseger.main.cache.entity.component.Coord_c;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.type.RectangleFunction_c;
import org.riseger.protoctl.search.function.FUNCTION;

public class Rectangle_fc extends RectangleFunction_c {

    public Rectangle_fc(int indexStart, SearchMemory memory, double threshold) {
        super(indexStart, memory, threshold);
    }

    @Override
    public void setFunction(FUNCTION condition) {

    }

    @Override
    public MBRectangle_c resolve(Element_c element) {
        Coord_c coord = (Coord_c) super.get(1);
        Double len = ((Number) super.get(2)).doubleValue();
        MBRectangle_c mbr = new MBRectangle_c(coord,len,super.getThreshold());
        super.set(mbr);
        return mbr;
    }
}

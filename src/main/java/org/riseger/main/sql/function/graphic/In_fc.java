package org.riseger.main.sql.function.graphic;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.MemoryConstant;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class In_fc extends BooleanFunction_c {

    public In_fc(SearchMemory memory, double threshold, CommandList commandList) {
        super(memory, threshold, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Element_c element = (Element_c) super.getMap(MemoryConstant.ELEMENT);
        MBRectangle_c r = (MBRectangle_c) super.poll();
        boolean result = r.inner(element);
        super.put(result);
    }
}

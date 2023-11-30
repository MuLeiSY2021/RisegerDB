package org.riseger.main.system.compile.function.graphic;

import org.riseger.main.system.cache.entity.component.Element_c;
import org.riseger.main.system.cache.entity.component.MBRectangle_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.serializer.JsonSerializer;

public class Out_fc extends Function_c {

    public Out_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Element_c element = (Element_c) searchMemory.get(MemoryConstant.ELEMENT);
        MBRectangle_c r = (MBRectangle_c) searchMemory.poll();
        boolean result = !r.intersects(element);
        LOG.debug(JsonSerializer.serializeToString(element.getCoordsSet()) + " OUT " + JsonSerializer.serializeToString(r.getCoordsSet()) + " = " + result);
        searchMemory.push(result);
    }
}

package org.riseger.main.system.compile.function.graphic;

import org.riseger.main.system.cache.component.Element;
import org.riseger.main.system.cache.component.GeoRectangle;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;
import org.riseger.protocol.serializer.JsonSerializer;

public class Out_fc extends Function_c {

    public Out_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Element element = (Element) searchMemory.get(MemoryConstant.ELEMENT);
        GeoRectangle r = (GeoRectangle) searchMemory.poll();
        boolean result = !r.intersects(element);
        LOG.debug(JsonSerializer.serializeToString(element.getCoordsSet()) + " OUT " + JsonSerializer.serializeToString(r.getCoordsSet()) + " = " + result);
        searchMemory.push(result);
    }
}

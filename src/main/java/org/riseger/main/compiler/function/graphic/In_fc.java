package org.riseger.main.compiler.function.graphic;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.serializer.JsonSerializer;

public class In_fc extends Function_c {

    public In_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Element_c element = (Element_c) super.get(MemoryConstant.ELEMENT);
        MBRectangle_c r = (MBRectangle_c) super.poll();
        boolean result = r.inner(element);
        LOG.debug(JsonSerializer.serializeToString(element.getCoordsSet()) + " IN " + JsonSerializer.serializeToString(r.getCoordsSet()) + " = " + result);

        super.put(result);
    }
}

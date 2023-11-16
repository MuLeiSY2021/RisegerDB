package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class UseMap_fc extends Function_c {

    public UseMap_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Database_c db = (Database_c) super.get(MemoryConstant.DATABASE);
        String name = (String) poll();
        LOG.debug("获取地图:\"" + name + "\"");
        MapDB_c map = db.getMap(name);
        super.setMap(map, MemoryConstant.MAP);
        super.setMap(map.getThreshold(),MemoryConstant.THRESHOLD);
    }

}

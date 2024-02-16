package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.cache.component.Database_c;
import org.riseger.main.system.cache.component.Map_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

public class UseMap_fc extends Function_c {

    public UseMap_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Database_c db = (Database_c) searchMemory.get(MemoryConstant.DATABASE);
        String name = (String) searchMemory.poll();
        LOG.debug("获取地图:\"" + name + "\"");
        Map_c map = db.getMap(name);
        searchMemory.setMap(map, MemoryConstant.MAP);
        searchMemory.setMap(map.getThreshold(), MemoryConstant.THRESHOLD);
    }

}

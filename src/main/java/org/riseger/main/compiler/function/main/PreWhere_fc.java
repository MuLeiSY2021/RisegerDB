package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.compoent.SearchSet;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.exception.SQLException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PreWhere_fc extends Function_c {

    public PreWhere_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        WhereIterator iterator;
        List<MapDB_c> maps = new LinkedList<>();
        maps.add((MapDB_c) get(MemoryConstant.MAP));
        MBRectangle_c scope = (MBRectangle_c) get(MemoryConstant.SCOPE);//√

        Map<String, SearchSet> searchMap = (Map<String, SearchSet>) get(MemoryConstant.MODEL);//√
        iterator = new WhereIterator(searchMap, maps, scope);
        setMap(iterator, MemoryConstant.WHERE);
        if (iterator.hasNext()) {
            MBRectangle_c mbRectangle = iterator.next();
            super.setMap(mbRectangle, MemoryConstant.ELEMENT);
        }
    }
}

package org.riseger.main.compiler.function.get;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.MapDB_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.MainFunction_c;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

import java.util.List;

public class GetMaps_fc extends MainFunction_c {

    public GetMaps_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        Database_c db = (Database_c) super.getMap(MemoryConstant.DATABASE);

        ResultSet resultSet;
        if (hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) super.getMap(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            super.setMap(resultSet, MemoryConstant.RESULT);
        }
        List<MapDB_c> mapDBs = db.getMapDBs();
        ResultModelSet resultModelSet = new ResultModelSet();
        resultSet.setModelSet("maps", resultModelSet);
        for (MapDB_c map : mapDBs) {
            ResultElement resultElement = new ResultElement();
            resultElement.addColumn("name", map.getName());
            resultElement.setAllKeyColumns(map.getCoordsSet());
            resultModelSet.add(resultElement);
        }
    }

}

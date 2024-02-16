package org.riseger.main.system.compile.function.get;

import org.riseger.main.system.cache.component.Database_c;
import org.riseger.main.system.cache.component.Map_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.exception.SQLException;

import java.util.List;

public class GetMaps_fc extends Function_c {

    public GetMaps_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        if (searchMemory.hasMap(MemoryConstant.DATABASE)) {
            throw new SQLException("No database using");
        }
        Database_c db = (Database_c) searchMemory.get(MemoryConstant.DATABASE);

        ResultSet resultSet;
        if (searchMemory.hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) searchMemory.get(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            searchMemory.setMap(resultSet, MemoryConstant.RESULT);
        }
        List<Map_c> mapDBs = db.getMapDBs();
        ResultModelSet resultModelSet = new ResultModelSet();
        resultSet.setModelSet("maps", resultModelSet);
        for (Map_c map : mapDBs) {
            ResultElement resultElement = new ResultElement();
            resultElement.addColumn("name", map.getName());
            //TODO: Maps not have their coordinates
            resultElement.setAllKeyColumns(map.getCoordsSet());
            resultModelSet.add(resultElement);
        }
    }

}

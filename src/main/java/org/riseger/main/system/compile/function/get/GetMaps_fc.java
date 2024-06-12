package org.riseger.main.system.compile.function.get;

import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.GeoMap;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.compiler.result.ResultElement;
import org.riseger.protocol.compiler.result.ResultModelSet;
import org.riseger.protocol.compiler.result.ResultSet;
import org.riseger.protocol.exception.SQLException;

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
        Database db = (Database) searchMemory.get(MemoryConstant.DATABASE);

        ResultSet resultSet;
        if (searchMemory.hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) searchMemory.get(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            searchMemory.setMap(resultSet, MemoryConstant.RESULT);
        }
        List<GeoMap> mapDBs = db.getMapDBs();
        ResultModelSet resultModelSet = new ResultModelSet();
        resultSet.setModelSet("maps", resultModelSet);
        for (GeoMap map : mapDBs) {
            ResultElement resultElement = new ResultElement();
            resultElement.addColumn("name", map.getName());
            resultModelSet.add(resultElement);
        }
    }

}

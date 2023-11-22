package org.riseger.main.system.compile.function.get;

import org.riseger.main.system.CacheSystem;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.exception.SQLException;

import java.util.List;

public class GetDatabases_fc extends Function_c {

    public GetDatabases_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        ResultSet resultSet;
        if (searchMemory.hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) searchMemory.get(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            searchMemory.setMap(resultSet, MemoryConstant.RESULT);
        }
        List<String> names = CacheSystem.INSTANCE.getDatabases();
        ResultModelSet resultModelSet = new ResultModelSet();
        resultSet.setModelSet("databases", resultModelSet);
        for (String name : names) {
            ResultElement resultElement = new ResultElement();
            resultElement.addColumn("name", name);
            resultModelSet.add(resultElement);
        }
    }

}

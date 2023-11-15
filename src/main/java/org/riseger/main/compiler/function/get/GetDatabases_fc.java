package org.riseger.main.compiler.function.get;

import org.riseger.main.cache.manager.CacheMaster;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.exception.SQLException;

import java.util.List;

public class GetDatabases_fc extends Function_c {

    public GetDatabases_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        ResultSet resultSet;
        if (hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) super.getMap(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            super.setMap(resultSet, MemoryConstant.RESULT);
        }
        List<String> names = CacheMaster.INSTANCE.getDatabases();
        ResultModelSet resultModelSet = new ResultModelSet();
        resultSet.setModelSet("databases", resultModelSet);
        for (String name : names) {
            ResultElement resultElement = new ResultElement();
            resultElement.addColumn("name", name);
            resultModelSet.add(resultElement);
        }
    }

}

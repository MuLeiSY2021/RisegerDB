package org.riseger.main.system.compile.function.get;

import org.riseger.main.system.cache.component.Database_c;
import org.riseger.main.system.cache.component.Model_c;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.struct.entity.Type;

import java.util.List;
import java.util.Map;

public class GetModels_fc extends Function_c {

    public GetModels_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Database_c db = (Database_c) searchMemory.get(MemoryConstant.DATABASE);

        ResultSet resultSet;
        if (searchMemory.hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) searchMemory.get(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            searchMemory.setMap(resultSet, MemoryConstant.RESULT);
        }
        List<Model_c> models = db.getModels();
        ResultModelSet resultModelSet = new ResultModelSet();
        resultSet.setModelSet("maps", resultModelSet);
        for (Model_c model : models) {
            ResultElement resultElement = new ResultElement();
            resultElement.addColumn("name", model.getName());
            resultElement.addColumn("parent", model.getParent());
            for (Map.Entry<String, Type> set : model.getParameters().entrySet()) {
                resultElement.addColumn(set.getKey(), set.getValue().name());
            }
            resultModelSet.add(resultElement);
        }
    }

}

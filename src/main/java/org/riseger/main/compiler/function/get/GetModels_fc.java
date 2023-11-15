package org.riseger.main.compiler.function.get;

import org.riseger.main.cache.entity.component.Database_c;
import org.riseger.main.cache.entity.component.Model_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.exception.SQLException;
import org.riseger.protoctl.struct.entity.Type;

import java.util.List;
import java.util.Map;

public class GetModels_fc extends Function_c {

    public GetModels_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        Database_c db = (Database_c) super.getMap(MemoryConstant.DATABASE);

        ResultSet resultSet;
        if (hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) super.getMap(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            super.setMap(resultSet, MemoryConstant.RESULT);
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

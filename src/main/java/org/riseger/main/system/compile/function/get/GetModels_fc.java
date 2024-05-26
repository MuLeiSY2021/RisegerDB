package org.riseger.main.system.compile.function.get;

import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.cache.component.Model;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.compiler.result.ResultElement;
import org.riseger.protocol.compiler.result.ResultModelSet;
import org.riseger.protocol.compiler.result.ResultSet;
import org.riseger.protocol.exception.SQLException;
import org.riseger.protocol.struct.entity.Type;

import java.util.List;
import java.util.Map;

public class GetModels_fc extends Function_c {

    public GetModels_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        Database db = (Database) searchMemory.get(MemoryConstant.DATABASE);

        ResultSet resultSet;
        if (searchMemory.hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) searchMemory.get(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            searchMemory.setMap(resultSet, MemoryConstant.RESULT);
        }
        List<Model> models = db.getModels();
        ResultModelSet resultModelSet = new ResultModelSet();
        resultSet.setModelSet("maps", resultModelSet);
        for (Model model : models) {
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

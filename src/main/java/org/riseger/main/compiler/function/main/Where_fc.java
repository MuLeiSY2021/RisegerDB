package org.riseger.main.compiler.function.main;

import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.Function_c;
import org.riseger.main.compiler.semantic.SemanticTree;
import org.riseger.protoctl.compiler.function.Entity_f;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.ProcessorFunction;
import org.riseger.protoctl.compiler.function.loop.Back_f;
import org.riseger.protoctl.compiler.function.loop.IfJump_f;
import org.riseger.protoctl.compiler.function.main.PostWhere_f;
import org.riseger.protoctl.compiler.function.main.PreWhere_f;
import org.riseger.protoctl.compiler.result.ResultElement;
import org.riseger.protoctl.compiler.result.ResultModelSet;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.protoctl.exception.SQLException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Where_fc extends Function_c implements ProcessorFunction {

    public Where_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws SQLException {
        if ((Boolean) poll()) {
            fillResult();
        }
        WhereIterator iterator = (WhereIterator) getMap(MemoryConstant.WHERE);
        if (iterator.hasNext()) {
            super.put(true);
        } else {
            super.put(false);
        }
    }

    private void fillResult() {
        if (!super.hasMap(MemoryConstant.RESULT)) {
            setMap(new ResultSet(), MemoryConstant.RESULT);
        }
        Element_c element = (Element_c) super.getMap(MemoryConstant.ELEMENT);
        ResultSet resultSet;
        if (hasMap(MemoryConstant.RESULT)) {
            resultSet = (ResultSet) super.getMap(MemoryConstant.RESULT);
        } else {
            resultSet = new ResultSet();
            super.setMap(resultSet, MemoryConstant.RESULT);
        }
        ResultModelSet resultModelSet;
        if (resultSet.hasModelSet(element.getModel())) {
            resultModelSet = resultSet.getModelSet(element.getModel());
        } else {
            resultModelSet = new ResultModelSet();
            resultSet.setModelSet(element.getModel(), resultModelSet);
        }
        Map<String, List<String>> searchList = (Map<String, List<String>>) getMap(MemoryConstant.SEARCH);//√

        ResultElement resultElement = new ResultElement();
        for (String column : searchList.get(element.getModel())) {
            if (column.startsWith("KEY_LOOP")) {
                String[] values = column.split("::");
                if (values.length == 1) {
                    resultElement.setAllKeyColumns(element.getCoordsSet());
                } else if (values.length == 2) {
                    int index = Integer.parseInt(values[1]);
                    resultElement.setKeyColumns(index, element.getCoordsSet()[index]);
                } else if (values.length == 3) {
                    int index = Integer.parseInt(values[1]),
                            x = values[2] == "x" ? 0 : 1;
                    resultElement.setKeyColumns(index, x, element.getCoordsSet()[index][x]);
                }
            } else {
                resultElement.addColumn(column, element.getAttributes().get(column));
            }
        }
        resultModelSet.add(resultElement);
    }

    @Override
    public void stretch(SemanticTree.Node node, int size, List<Function_f> functionList) {
        node.addHead(new Back_f());
        node.addHead(new Entity_f(size + 1));
        node.addHead(new PostWhere_f());
        node.addHead(new IfJump_f());
        node.addHead(new Entity_f(node.getLevel() + 5));
    }

    @Override
    public List<Function_f> preprocess() {
        List<Function_f> result = new LinkedList<>();
        result.add(new PreWhere_f());
        return result;
    }
}

package org.riseger.main.compiler.function.logic;

import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.BooleanFunction_c;
import org.riseger.main.compiler.semantic.SemanticTree;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.ProcessorFunction;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

import java.util.List;

public class And_fc extends BooleanFunction_c implements ProcessorFunction {

    public And_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        boolean f1 = (Boolean) super.poll(),
                f2 = (Boolean) super.poll();
        boolean result = f1 & f2;
        LOG.debug(f1 + " & " + f2 + " " + result);
        super.put(result);
    }

    @Override
    public void stretch(SemanticTree.Node node, int size, List<Function_f> functionList) {
    }

    @Override
    public List<Function_f> preprocess() {
        return null;
    }
}

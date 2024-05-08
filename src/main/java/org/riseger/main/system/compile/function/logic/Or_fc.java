package org.riseger.main.system.compile.function.logic;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.main.system.compile.function.interfaces.StretchFunction;
import org.riseger.main.system.compile.semantic.SemanticTree;
import org.riseger.protocol.compiler.function.Entity_f;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.ProcessorFunction;
import org.riseger.protocol.compiler.function.loop.IfJump_f;
import org.riseger.protocol.exception.SQLException;

import java.util.List;

public class Or_fc extends Function_c implements ProcessorFunction, StretchFunction {

    public Or_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        boolean f1 = (Boolean) searchMemory.poll();
        boolean f2 = (Boolean) searchMemory.poll();
        boolean result = f1 | f2;
        LOG.debug(f1 + " | " + f2 + " " + result);
        searchMemory.push(result);
    }

    @Override
    public void stretch(SemanticTree.Node node, int size, List<Function_f> functionList) {
        node.addChild(new Entity_f(node.getLevel()), 0);
        node.addChild(new IfJump_f(), 0);
    }

    @Override
    public List<Function_f> preProcess() {
        return null;
    }

}

package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.main.system.compile.function.interfaces.StretchFunction;
import org.riseger.main.system.compile.semantic.SemanticTree;
import org.riseger.protocol.compiler.function.Entity_f;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.ProcessorFunction;
import org.riseger.protocol.compiler.function.loop.Back_f;
import org.riseger.protocol.compiler.function.loop.IfJump_f;
import org.riseger.protocol.compiler.function.main.PostWhere_f;
import org.riseger.protocol.compiler.function.main.PreWhere_f;
import org.riseger.protocol.exception.SQLException;

import java.util.LinkedList;
import java.util.List;

public class Where_fc extends Function_c implements ProcessorFunction, StretchFunction {

    public Where_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        if ((Boolean) searchMemory.poll()) {
            WhereHandleFunction function = (WhereHandleFunction) searchMemory.get(MemoryConstant.METOD_PROCESS);
            function.postProcess(searchMemory, commandList);
        }
        WhereIterator iterator = (WhereIterator) searchMemory.get(MemoryConstant.WHERE);
        if (iterator.hasNext()) {
            searchMemory.push(false);
        } else {
            searchMemory.push(true);
        }
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
    public List<Function_f> preProcess() {
        List<Function_f> result = new LinkedList<>();
        result.add(new PreWhere_f());
        return result;
    }
}

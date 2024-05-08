package org.riseger.main.system.compile.function.main;

import org.riseger.main.system.LogSystem;
import org.riseger.main.system.cache.component.Database;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.MemoryConstant;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.main.system.compile.function.interfaces.StretchFunction;
import org.riseger.main.system.compile.semantic.SemanticTree;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.main.WriteUpdateLog_f;
import org.riseger.protocol.exception.SQLException;

import java.util.List;

public class WiteUpdateLog_fc extends Function_c implements StretchFunction {
    public WiteUpdateLog_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        LogSystem.INSTANCE.writeLog(searchMemory.getSessionId(), ((Database) searchMemory.get(MemoryConstant.DATABASE)).getName(), commandList);
    }

    @Override
    public void stretch(SemanticTree.Node node, int size, List<Function_f> functionList) {
        node.addHead(new WriteUpdateLog_f());
    }
}

package org.riseger.main.compiler.compoent;

import lombok.Data;
import org.riseger.main.compiler.function.type.Function_c;
import org.riseger.main.compiler.lextcal.Lexicator;
import org.riseger.main.compiler.semantic.SemanticTree;
import org.riseger.main.compiler.syntax.Parser;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.Tokenizer;
import org.riseger.protoctl.search.result.ResultSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class SearchSession {
    private final Tokenizer tokenizer;

    private final Lexicator lexicator;

    private final String sourcecode;

    private final Parser parser;

    private final Map<Integer, Object> constTable = new HashMap<>();

    private final Map<Object, Integer> indexTable = new HashMap<>();
    private final CommandList commandList = new CommandList();
    private final SearchMemory memory = new SearchMemory();
    private List<Token> tokenList;

    public SearchSession(String sourcecode, Tokenizer tokenizer, Lexicator lexicator, Parser parser) {
        this.sourcecode = sourcecode;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.lexicator = lexicator;
    }

    public SearchSession(SemanticTree semanticTree) {
        this.sourcecode = null;
        this.tokenizer = null;
        this.parser = null;
        this.lexicator = null;
        semanticTree.getFunctionList(memory, commandList);
    }


    public void preHandle() {
        if (sourcecode != null) {
            this.tokenList = this.tokenizer.invoke(sourcecode);
            this.lexicator.invoke(tokenList, this);
            SemanticTree semanticTree = this.parser.invoke(tokenList, this);
            semanticTree.getFunctionList(memory, commandList);
        }
    }

    public ResultSet process() throws Exception {
        preHandle();
        for (Function_c function = commandList.next(); commandList.hasNext(); function = commandList.next()) {
            function.process();
        }
        return (ResultSet) memory.getMapValue(MemoryConstant.RESULT);
    }

    public int put(Object tmp) {
        int i;
        if (!indexTable.containsKey(tmp)) {
            i = constTable.size();
            constTable.put(i, tmp);
            indexTable.put(tmp, i);
        } else {
            i = indexTable.get(tmp);
        }
        return i;
    }

    public Object get(int id) {
        return this.constTable.get(id);
    }
}

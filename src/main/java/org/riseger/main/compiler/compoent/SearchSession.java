package org.riseger.main.compiler.compoent;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.function.type.Function_c;
import org.riseger.main.compiler.lextcal.Lexicator;
import org.riseger.main.compiler.semantic.SemanticTree;
import org.riseger.main.compiler.syntax.Parser;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.Tokenizer;
import org.riseger.protoctl.compiler.result.ResultSet;
import org.riseger.utils.Utils;

import java.util.ArrayList;

@Data
public class SearchSession {
    public static final Logger LOG = Logger.getLogger(SearchSession.class);

    private final Tokenizer tokenizer;

    private final Lexicator lexicator;

    private final Parser parser;

    private CommandList commandList = new CommandList();

    private final SearchMemory memory = new SearchMemory();

    private String sourcecode;

    private ArrayList<Token> tokenList;


    public SearchSession(String sourcecode, Tokenizer tokenizer, Lexicator lexicator, Parser parser) {
        this.sourcecode = sourcecode;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.lexicator = lexicator;
    }

    public void preHandle() throws Exception {
        if (sourcecode != null) {
            this.tokenList = this.tokenizer.invoke(sourcecode);
            this.lexicator.invoke(tokenList, this);
            SemanticTree semanticTree = this.parser.invoke(tokenList);
            semanticTree.getFunctionList(memory, commandList);
        }
    }

    public ResultSet process() throws Exception {
        preHandle();
        if (commandList.isEmpty()) {
            LOG.debug("No command");
            return null;
        }
        for (Function_c function; commandList.hasNext(); ) {
            function = commandList.next();
            LOG.debug("ID:" + commandList.index() + " Fun: " + Utils.getClassLastDotName(function.getClass()));
            function.process();
        }
        ResultSet resultSet = (ResultSet) memory.getMapValue(MemoryConstant.RESULT);
        if (resultSet == null) {
            return ResultSet.empty();
        }
        return resultSet;
    }

    public void reset() {
        this.commandList = new CommandList();
    }

}

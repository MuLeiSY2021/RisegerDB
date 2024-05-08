package org.riseger.main.system.compile.compoent;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.main.system.compile.lextcal.Lexicator;
import org.riseger.main.system.compile.semantic.SemanticTree;
import org.riseger.main.system.compile.syntax.Parser;
import org.riseger.main.system.compile.token.Token;
import org.riseger.main.system.compile.token.Tokenizer;
import org.riseger.protocol.compiler.CommandTree;
import org.riseger.protocol.compiler.result.ResultSet;
import org.riseger.utils.Utils;

import java.util.ArrayList;

@Data
public class SearchSession {
    public static final Logger LOG = Logger.getLogger(SearchSession.class);

    private final Tokenizer tokenizer;

    private final Lexicator lexicator;

    private final Parser parser;

    private final int sessionId;

    private CommandTree commandTree;

    private CommandList commandList = new CommandList();

    private final SearchMemory memory = new SearchMemory(this);

    private String sourcecode;

    private ArrayList<Token> tokenList;


    public SearchSession(String sourcecode, Tokenizer tokenizer, Lexicator lexicator, Parser parser, int sessionId) {
        this.sourcecode = sourcecode;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.lexicator = lexicator;
        this.sessionId = sessionId;
    }

    public SearchSession(CommandTree commandTree, Tokenizer tokenizer, Lexicator lexicator, Parser parser, int sessionId) {
        this.commandTree = commandTree;
        this.tokenizer = tokenizer;
        this.parser = parser;
        this.lexicator = lexicator;
        this.sessionId = sessionId;
    }

    public void preHandle() throws Exception {
        if (sourcecode == null) {
            SemanticTree semanticTree = new SemanticTree(commandTree);
            semanticTree.getFunctionList(memory, commandList);
        } else {
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
            function.process(memory, commandList);
        }
        ResultSet resultSet = (ResultSet) memory.get(MemoryConstant.RESULT);
        if (resultSet == null) {
            return ResultSet.empty();
        } else {
            resultSet.pack();
        }
        return resultSet;
    }

    public void reset() {
        this.commandList = new CommandList();
        this.memory.removeMapValue(MemoryConstant.RESULT);
        this.memory.removeMapValue(MemoryConstant.WHERE);
        this.memory.removeMapValue(MemoryConstant.ELEMENT);
    }

}

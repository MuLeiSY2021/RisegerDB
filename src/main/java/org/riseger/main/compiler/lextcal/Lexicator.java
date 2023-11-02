package org.riseger.main.compiler.lextcal;

import org.apache.log4j.Logger;
import org.riseger.main.compiler.CompilerConstant;
import org.riseger.main.compiler.compoent.SearchSession;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.TokenType;
import org.riseger.utils.tree.MultiBranchesTree;

import java.util.List;
import java.util.regex.Pattern;

public class Lexicator {
    private static final Logger LOG = Logger.getLogger(Lexicator.class);
    private final MultiBranchesTree<Keyword> tree;

    private final Pattern numberPattern = Pattern.compile(CompilerConstant.NUMBER_PATTERN);

    private final Pattern wordPattern = Pattern.compile(CompilerConstant.WORD_PATTERN);

    public Lexicator(MultiBranchesTree<Keyword> tree) {
        this.tree = tree;
    }

    public void invoke(List<Token> tokenList, SearchSession session) {
        for (Token token : tokenList) {
            String sourcecode = token.getSourceCode();
            if (sourcecode.isEmpty()) {
                continue;
            } else if (numberPattern.matcher(sourcecode).matches()) {
                LOG.debug("SourceCode:" + sourcecode + " 匹配数字");
                double tmp = Double.parseDouble(sourcecode);
                token.set(Double.hashCode(tmp), TokenType.NUMBER);
                continue;
            }
            String tmp = sourcecode.toUpperCase();
            Keyword keyword = this.tree.search(C.toCollection(tmp));
            if (keyword != null) {
                LOG.debug("SourceCode:" + sourcecode + " 匹配关键字:" + keyword.getCode());
                token.set(keyword.getId(), TokenType.KEYWORD);
            } else if (wordPattern.matcher(sourcecode).matches()) {
                LOG.debug("SourceCode:" + sourcecode + " 匹配字符串");
                token.set(sourcecode.hashCode(), TokenType.STRING);
            } else {
                LOG.error("非法字符存在:" + token.getSourceCode(), new IllegalArgumentException());
                throw new IllegalStateException();
            }
        }
    }
}

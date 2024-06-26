package org.riseger.main.system.compile.lextcal;

import org.apache.log4j.Logger;
import org.riseger.main.system.compile.CompilerConstant;
import org.riseger.main.system.compile.compoent.SearchSession;
import org.riseger.main.system.compile.token.Token;
import org.riseger.main.system.compile.token.TokenType;
import org.riseger.utils.tree.MultiBranchesTree;

import java.util.List;
import java.util.regex.Pattern;

public class Lexicator {
    private static final Logger LOG = Logger.getLogger(Lexicator.class);
    private final MultiBranchesTree<Keyword> tree;

    private final Pattern numberPattern = Pattern.compile(CompilerConstant.NUMBER_PATTERN);

    private final Pattern wordPattern = Pattern.compile(CompilerConstant.WORD_PATTERN);

    private final Pattern attrPattern = Pattern.compile(CompilerConstant.ATTRIBUTE_PATTERN);

    public Lexicator(MultiBranchesTree<Keyword> tree) {
        this.tree = tree;
    }

    public void invoke(List<Token> tokenList, SearchSession session) throws Exception {
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
            } else if (attrPattern.matcher(sourcecode).matches()) {
                LOG.debug("SourceCode:" + sourcecode + " 匹配属性名");
                token.set(sourcecode.hashCode(), TokenType.ATTRIBUTE);
            } else {
                LOG.error("非法字符存在:" + token.getSourceCode(), new IllegalArgumentException());
                throw new IllegalStateException();
            }
        }
    }
}

package org.riseger.main.compiler.lextcal;

import org.riseger.main.compiler.CompilerConstant;
import org.riseger.main.compiler.session.Session;
import org.riseger.main.compiler.token.Token;
import org.riseger.main.compiler.token.TokenType;
import org.riseger.utils.tree.MultiBranchesTree;

import java.util.List;
import java.util.regex.Pattern;

public class Lexicator {
    private final MultiBranchesTree<Keyword> tree;

    private final Pattern numberPattern = Pattern.compile(CompilerConstant.NUMBER_PATTERN);

    private final Pattern wordPattern = Pattern.compile(CompilerConstant.WORD_PATTERN);

    public Lexicator(MultiBranchesTree<Keyword> tree) {
        this.tree = tree;
    }

    public void invoke(List<Token> tokenList, Session session) {
        for (Token token : tokenList) {
            String sourcecode = token.getSourceCode();
            if (numberPattern.matcher(sourcecode).matches()) {
                double tmp = Double.parseDouble(sourcecode);
                int id = session.put(tmp);
                token.set(id, TokenType.NUMBER);
                continue;
            }
            sourcecode = sourcecode.toUpperCase();
            Keyword keyword = this.tree.search(C.toCollection(sourcecode));
            if (keyword != null) {
                token.set(keyword.getId(), TokenType.KEYWORD);
                continue;
            }

            if (wordPattern.matcher(sourcecode).matches()) {
                int id = session.put(sourcecode);
                token.set(id, TokenType.STRING);
            } else {
                System.out.println("Word:" + token);
                throw new IllegalArgumentException("非法字符存在");
            }
        }
    }
}

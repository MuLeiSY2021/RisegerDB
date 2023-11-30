package org.riseger.main.system.compile.token;

import org.riseger.main.system.compile.CompilerConstant;
import org.riseger.main.system.compile.lextcal.C;
import org.riseger.main.system.compile.lextcal.Keyword;
import org.riseger.utils.tree.MultiBranchesTree;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    static final Pattern tokenPattern = Pattern.compile(CompilerConstant.TOKEN_PATTERN);

    private final MultiBranchesTree<Keyword> keywordsTree;

    public Tokenizer(MultiBranchesTree<Keyword> keywordsTree) {
        this.keywordsTree = keywordsTree;
    }

    public ArrayList<Token> invoke(String sourcecode) throws Exception {
        ArrayList<Token> lines;
        lines = splitLine(sourcecode);
        lines = splitSpace(lines);
        lines = splitToken(lines);
        return lines;
    }

    private ArrayList<Token> splitToken(List<Token> lines) throws Exception {
        ArrayList<Token> tokens = new ArrayList<>();
        for (Token token : lines) {
            String tile = token.getSourceCode();
            if (!tile.isEmpty()) {
                Matcher m = tokenPattern.matcher(tile);
                int prev = 0;
                while (m.find()) {
                    if (m.start() != prev) {
                        String symbols = tile.substring(prev, m.start());
                        Keyword keyword;
                        while (!symbols.isEmpty() && (keyword = keywordsTree.find(C.toCollection(symbols))) != null) {
                            tokens.add(new Token(symbols.substring(0, keyword.size()), prev, token.getLine()));
                            symbols = symbols.substring(keyword.getWords().size());
                        }
                    }
                    tokens.add(new Token(tile.substring(m.start(), m.end()), m.start(), token.getLine()));
                    prev = m.end();
                }
                if (tile.length() - 1 >= prev) {
                    String symbols = tile.substring(prev);
                    Keyword keyword;
                    while (!symbols.isEmpty() && (keyword = keywordsTree.find(C.toCollection(symbols))) != null) {
                        tokens.add(new Token(symbols.substring(0, keyword.size()), prev, token.getLine()));
                        symbols = symbols.substring(keyword.getWords().size());
                    }
                }
            }
        }
        return tokens;
    }

    private ArrayList<Token> splitLine(String code) {
        ArrayList<Token> res = new ArrayList<>();
        int i = 1;
        for (String line : code.split("\n")) {
            res.add(new Token(line, i, 0));
            i++;
        }
        return res;
    }

    private ArrayList<Token> splitSpace(List<Token> tokens) {
        ArrayList<Token> res = new ArrayList<>();

        for (Token token : tokens) {
            String line = token.getSourceCode();
            //查找注释
            Matcher m = Pattern.compile("//.+").matcher(line);
            if (m.find()) {
                //去除注释部分
                continue;
            }
            //去除空句
            if (line.isEmpty()) {
                continue;
            }
            int top = 1, bottom = 1;
            boolean flag = false;
            StringBuilder sb = new StringBuilder();
            for (Character c : line.toCharArray()) {
                if (!c.equals(' ')) {
                    if (flag) {
                        top = bottom;
                        flag = false;
                    }
                    sb.append(c);
                } else if (sb.length() != 0) {
                    res.add(new Token(sb.toString(), token.getLine(), top));
                    flag = true;
                    sb = new StringBuilder();
                }
                bottom++;
            }
            if (sb.length() > 0) {
                res.add(new Token(sb.toString(), token.getLine(), top));
            }
        }

        return res;
    }


}

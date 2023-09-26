package org.riseger.main.compiler.token;

import com.google.gson.Gson;
import org.riseger.main.compiler.CompilerConstant;
import org.riseger.main.compiler.lextcal.KeywordsTree;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    static Pattern tokenPattern = Pattern.compile(CompilerConstant.TOKEN_PATTERN);

    private final KeywordsTree keywordsTree;

    public Tokenizer(KeywordsTree keywordsTree) {
        this.keywordsTree = keywordsTree;
    }

    public List<Token> invoke(String sourcecode) {
        List<Token> lines;
        lines = splitLine(sourcecode);
        lines = splitSpace(lines);
        lines = splitToken(lines);
        return lines;
    }

//    public List<String> splitToken(String tile) {
//        List<String> res = new ArrayList<>();
//        Matcher m;
//        while (tile.length() > 0) {
//            int index = -1;
//            int tmp;
//            if ((m = tokenPattern.matcher(tile)).find() && m.start() == 0) {
//                index = m.end(0);
//            } else if ((tmp = this.keywordsTree.getIndex(tile)) > -1) {
//                index = tmp;
//            }
//
//            if (index == -1) {
//                throw new IndexOutOfBoundsException(tile + " -1");
//            }
//            res.add(tile.substring(0, index));
//            if (tile.length() == index) {
//                break;
//            }
//            tile = tile.substring(index);
//        }
//        return res;
//    }

    private List<Token> splitToken(List<Token> lines) {
        List<Token> tokens = new LinkedList<>();
        for (Token token : lines) {
            int top = 0;
            String tile = token.getSourceCode();
            Matcher m;
            int debug = 100, round = 0;
            while (tile.length() > 0) {
                if (round == debug) {
                    throw new RuntimeException("Always Loop :" + tile + " token:" + new Gson().toJson(token));
                }
                int index = -1;
                int tmp;
                if ((m = tokenPattern.matcher(tile)).find() && m.start() == 0) {
                    index = m.end(0);
                } else if ((tmp = keywordsTree.getIndex(tile)) > -1) {
                    index = tmp;
                }

                if (index == -1) {
                    throw new IndexOutOfBoundsException(tile + " -1");
                }
                tokens.add(new Token(tile.substring(0, index), token.getLine(), top + token.getColumn()));
                top = index;

                if (token.getSourceCode().length() == index) {
                    break;
                }
                tile = tile.substring(index);
                round++;
            }
        }
        return tokens;
    }

    private List<Token> splitLine(String code) {
        List<Token> res = new LinkedList<>();
        int i = 1;
        for (String line : code.split("\n")) {
            res.add(new Token(line, i, 0));
            i++;
        }
        return res;
    }

    private List<Token> splitSpace(List<Token> tokens) {
        List<Token> res = new LinkedList<>();

        for (Token token : tokens) {
            String line = token.getSourceCode();
            //查找注释
            Matcher m = Pattern.compile("//.+").matcher(line);

            if (m.find()) {
                //去除注释部分
                line = line.substring(0, m.start());
            }
            //去除空句
            if (line.length() == 0) {
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
                } else {
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

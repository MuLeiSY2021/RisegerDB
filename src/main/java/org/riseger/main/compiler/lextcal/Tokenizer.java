package org.riseger.main.compiler.lextcal;

import com.google.gson.Gson;
import org.riseger.main.compiler.CompilerConstant;
import org.riseger.main.compiler.session.Context;
import org.riseger.main.compiler.session.Token;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    static Pattern tokenPattern = Pattern.compile(CompilerConstant.TOKEN_PATTERN);

    private final Map<String, Integer> wordMap = new HashMap<>();

    private final Map<Double, Integer> doubleMap = new HashMap<>();

    private final KeywordsTree keywordsTree;

    Pattern numberPattern = Pattern.compile(CompilerConstant.NUMBER_PATTERN);

    Pattern wordPattern = Pattern.compile(CompilerConstant.WORD_PATTERN);

    public Tokenizer(KeywordsTree keywordsTree) {
        this.keywordsTree = keywordsTree;
    }

    public void tokenize(Context context) {
        List<Token> lines;
        String sourcecode = context.getSourcecode();
        lines = splitLine(sourcecode);
        lines = splitSpace(lines);
        splitToken(lines, context);

        for (Token word : context.getTokens()) {
            setCode(word, context);
        }
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

    private void splitToken(List<Token> lines, Context context) {
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
                context.add(new Token(tile.substring(0, index), token.getLine(), top + token.getColumn()));
                top = index;

                if (token.getSourceCode().length() == index) {
                    break;
                }
                tile = tile.substring(index);
                round++;
            }
        }
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

    public void setCode(Token word, Context context) {
        String sourcecode = word.getSourceCode();
        if (numberPattern.matcher(sourcecode).matches()) {
            double tmp = Double.parseDouble(sourcecode);
            if (!doubleMap.containsKey(tmp)) {
                doubleMap.put(tmp, doubleMap.size() + 1);
                context.put(doubleMap.size(), tmp);
            }
            word.setCode(CompilerConstant.NUMBER_PREFIX + "_" + doubleMap.size());
            return;
        }
        sourcecode = sourcecode.toUpperCase();
        Keyword keyword = this.keywordsTree.get(sourcecode);
        if (keyword != null) {
            word.setCode(keyword.getCode());
            return;
        }

        if (wordPattern.matcher(sourcecode).matches()) {
            if (!wordMap.containsKey(sourcecode)) {
                wordMap.put(sourcecode, wordMap.size() + 1);
                context.put(wordMap.size(), sourcecode);
            }
            word.setCode(CompilerConstant.STRING_PREFIX + "_" + wordMap.size());
        } else {
            System.out.println("Word:" + word);
            throw new IllegalArgumentException("非法字符存在");
        }
    }
}

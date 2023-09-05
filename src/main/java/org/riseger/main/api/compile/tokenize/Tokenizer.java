package org.riseger.main.api.compile.tokenize;

import com.google.gson.Gson;
import org.riseger.main.api.compile.config.Config;
import org.riseger.main.api.compile.context.Context;
import org.riseger.main.api.compile.tree.KeywordsTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    static Pattern tokenPattern = Pattern.compile(Config.TOKEN_PATTERN);

    private final Context context;

    public Tokenizer(Context context) {
        this.context = context;
    }

    public static List<String> splitToken(String tile) {
        List<String> res = new ArrayList<>();
        Matcher m;
        while (tile.length() > 0) {
            int index = -1;
            int tmp;
            if ((m = tokenPattern.matcher(tile)).find() && m.start() == 0) {
                index = m.end(0);
            } else if ((tmp = KeywordsTree.INSTANCE.getIndex(tile)) > -1) {
                index = tmp;
            }

            if (index == -1) {
                throw new IndexOutOfBoundsException(tile + " -1");
            }
            res.add(tile.substring(0, index));
            if (tile.length() == index) {
                break;
            }
            tile = tile.substring(index);
        }
        return res;
    }

    public void execute() {
        List<Token> lines;
        String sourcecode = context.getSourcecode();
        lines = splitLine(sourcecode);
        lines = splitSpace(lines);
        splitToken(lines);
    }

    private void splitToken(List<Token> lines) {
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
                } else if ((tmp = KeywordsTree.INSTANCE.getIndex(tile)) > -1) {
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

}

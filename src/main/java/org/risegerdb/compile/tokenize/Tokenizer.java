package org.risegerdb.compile.tokenize;

import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.lextcal.LexicalTree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    public static final Tokenizer INSTANCE = new Tokenizer();

    static Pattern tokenPattern = Pattern.compile(CompileConfig.TOKEN_PATTERN);

    public List<String> execute(String code) {
        List<String> lines = new LinkedList<>(Arrays.asList(code.split("\n")));
        return tokenize(lines);
    }

    private List<String> getTokens(String tile) {
        Matcher m;
        List<String> tokens = new LinkedList<>();
        while (tile.length() > 0) {
            int index = -1;
            int tmp;
            if ((m = tokenPattern.matcher(tile)).find() && m.start() == 0) {
                index = m.end(0);
            } else if ((tmp = LexicalTree.INSTANCE.getIndex(tile)) > -1) {
                index = tmp;
            }

            if(index == -1) {
                throw new IndexOutOfBoundsException(tile + " -1");
            }
            tokens.add(tile.substring(0,index));
            tile = tile.substring(index);
        }
        return tokens;
    }

    private List<String> tokenize(List<String> tokens) {
        List<String> res = new LinkedList<>();

        for (String token:tokens) {

            //查找注释
            Matcher m = Pattern.compile("//.+").matcher(token);
            if(m.find()) {
                //去除注释部分
                token = token.substring(0, m.start());
            }

            //按照空格分词
            String[] tiles = token.split(" ");
            for (String tile:tiles) {
                res.addAll(getTokens(tile));
            }
        }

        return res;
    }



}

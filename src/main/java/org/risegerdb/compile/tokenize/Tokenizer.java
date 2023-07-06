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
                while (tile.length() > 0) {

                    String keyword = tile;
                    m = tokenPattern.matcher(keyword);
                    if(m.find()){
                        int start = m.start();

                        //说明前面有东西 “>=-5” 把前面的部分拆出来
                        if (start != 0) {
                            //拿出 >=-
                            keyword = tile.substring(0,start);
                        } else {
                            //说明前面没东西了，利用正则表达式把里面的第一部分拆出来
                            res.add(tile.substring(0, m.end()));
                            //把剩下的东西再填回
                            tile = tile.substring(m.end());
                            continue;
                        }
                    }
                    int log = 0;
                    //拿出 >=-
                    for ( int index_ = 0;index_ < keyword.length();index_ = 0) {
                        //从词法树里拿出>=
                        index_ = LexicalTree.INSTANCE.getIndex(keyword);
                        if (index_ == -1) {
                            throw new IllegalArgumentException("Invalid token word:" + keyword);
                        }
                        //把>=放入结果集
                        res.add(keyword.substring(0, index_));

                        //把剩下的-再次放入循环
                        keyword = keyword.substring(index_);
                        log = index_;
                    }
                    tile = tile.substring(log);
                }
            }
        }

        return res;
    }

    public List<String> execute(String code) {
        List<String> lines = new LinkedList<>(Arrays.asList(code.split("\n")));
        return tokenize(lines);

    }

}

package org.riseger.main.api.compile.lextcal;

import org.riseger.main.api.compile.config.Config;
import org.riseger.main.api.compile.context.Context;
import org.riseger.main.api.compile.tokenize.Token;
import org.riseger.main.api.compile.tree.Keyword;
import org.riseger.main.api.compile.tree.KeywordsTree;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TokenAnalyzer {

    private final Map<String, Integer> wordMap = new HashMap<>();
    private final Map<Double, Integer> doubleMap = new HashMap<>();
    private final Context context;
    Pattern numberPattern = Pattern.compile(Config.NUMBER_PATTERN);
    Pattern wordPattern = Pattern.compile(Config.WORD_PATTERN);

    public TokenAnalyzer(Context context) {
        this.context = context;
    }

    public void execute() {
        for (Token word : context.getTokens()) {
            setCode(word);
        }
    }

    public void setCode(Token word) {
        String sourcecode = word.getSourceCode();
        if (numberPattern.matcher(sourcecode).matches()) {
            double tmp = Double.parseDouble(sourcecode);
            if (!doubleMap.containsKey(tmp)) {
                doubleMap.put(tmp, doubleMap.size() + 1);
                context.put(doubleMap.size(), tmp);
            }
            word.setCode(Config.NUMBER_PREFIX + "_" + doubleMap.size());
            return;
        }
        sourcecode = sourcecode.toUpperCase();
        Keyword keyword = KeywordsTree.INSTANCE.get(sourcecode);
        if (keyword != null) {
            word.setCode(keyword.getCode());
            return;
        }

        if (wordPattern.matcher(sourcecode).matches()) {
            if (!wordMap.containsKey(sourcecode)) {
                wordMap.put(sourcecode, wordMap.size() + 1);
                context.put(wordMap.size(), sourcecode);
            }
            word.setCode(Config.STRING_PREFIX + "_" + wordMap.size());
        } else {
            System.out.println("Word:" + word);
            throw new IllegalArgumentException("非法字符存在");
        }
    }

}

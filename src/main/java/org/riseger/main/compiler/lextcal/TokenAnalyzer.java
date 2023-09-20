package org.riseger.main.compiler.lextcal;

import org.riseger.main.compiler.CompilerConstant;
import org.riseger.main.compiler.Context;
import org.riseger.main.compiler.Token;
import org.riseger.main.compiler.keyword.Keyword;
import org.riseger.main.compiler.keyword.KeywordsTree;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TokenAnalyzer {

    private final Map<String, Integer> wordMap = new HashMap<>();
    private final Map<Double, Integer> doubleMap = new HashMap<>();
    private final Context context;
    Pattern numberPattern = Pattern.compile(CompilerConstant.NUMBER_PATTERN);
    Pattern wordPattern = Pattern.compile(CompilerConstant.WORD_PATTERN);

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
            word.setCode(CompilerConstant.NUMBER_PREFIX + "_" + doubleMap.size());
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
            word.setCode(CompilerConstant.STRING_PREFIX + "_" + wordMap.size());
        } else {
            System.out.println("Word:" + word);
            throw new IllegalArgumentException("非法字符存在");
        }
    }

}

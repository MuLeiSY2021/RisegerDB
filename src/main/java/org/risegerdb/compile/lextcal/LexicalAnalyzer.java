package org.risegerdb.compile.lextcal;

import org.risegerdb.compile.config.CompileConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    LexicalTree lexicalTree = LexicalTree.INSTANCE;

    Pattern numberPattern = Pattern.compile(CompileConfig.NUMBER_PATTERN);

    Pattern wordPattern = Pattern.compile(CompileConfig.WORD_PATTERN);

    private final Map<String,Integer> wordMap = new HashMap<>();

    private final Map<Double,Integer> doubleMap = new HashMap<>();

    public LexicalAnalyzerOutput execute(List<String> lexicons) {
        LexicalAnalyzerOutput output = new LexicalAnalyzerOutput();
        for (String word:lexicons) {
            Lexicon lexicon = getLexicon(word);
            output.add(lexicon);
        }
        return output;
    }

    public Lexicon getLexicon(String word) {
        Function func;

        if(numberPattern.matcher(word).matches()) {
            double tmp = Double.parseDouble(word);
            if(!doubleMap.containsKey(tmp)) {
                doubleMap.put(tmp,doubleMap.size() + 1);
            }
            return new Lexicon(CompileConfig.NUMBER_CONST_PREFIX + "_" + doubleMap.get(tmp), tmp);
        }
        String tmp = word.toUpperCase();
        if((func = lexicalTree.get(tmp)) != null) {
            return new Lexicon(func.getId(), func.getKey(), Lexicon.Type.FUN);
        }
        if(wordPattern.matcher(word).matches()) {
            if(!wordMap.containsKey(word)) {
                wordMap.put(word,wordMap.size() + 1);
            }
            return new Lexicon(CompileConfig.STRING_CONST_PREFIX + "_" + wordMap.get(word), word, Lexicon.Type.STR);
        }else {
            System.out.println(word);

            throw new IllegalArgumentException("非法字符存在");
        }
    }

}

package org.riseger.main.compiler.lextcal;

import lombok.Data;
import org.riseger.main.compiler.CompilerConstant;

import java.util.LinkedList;
import java.util.List;

@Data
public class Keyword {
    private static final List<Keyword> KEYWORDS = new LinkedList<>();
    private final int keywordIndex;
    private final List<Character> words = new LinkedList<>();
    private final String code;

    public Keyword(String keyword, int index) {
        if (keyword.endsWith("\n")) {
            keyword = keyword.substring(0, keyword.length() - 1);
        }
        for (Character character : keyword.toCharArray()) {
            words.add(character);
        }
        this.keywordIndex = index;
        this.code = CompilerConstant.KEYWORD_PREFIX + CompilerConstant.SPLIT_PREFIX + KEYWORDS.size();
    }

    public static Keyword addKeyword(String word) {
        Keyword keyword = new Keyword(word, KEYWORDS.size());
        KEYWORDS.add(keyword);
        return keyword;
    }

    public static List<Keyword> getKeywords() {
        return KEYWORDS;
    }

    public char getWords(int index) {
        return this.words.get(index);
    }

    public int length() {
        return words.size();
    }

    public boolean isTail(int index) {
        return this.words.size() == index + 1;
    }

    public boolean equals(int index, char word) {
        return this.words.get(index) == word;
    }
}

package org.riseger.compile.tree;

import lombok.Data;
import org.riseger.compile.config.Config;
import org.riseger.protoctl.utils.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Data
public class Keyword {
    public static final List<Keyword> KEYWORDS = new LinkedList<>();

    static {
        String text = Utils.getText(new File("data/keyword.txt"));
        if (text != null) {
            for (String keyword : text.split("\n")) {
                KEYWORDS.add(new Keyword(keyword.toUpperCase()));
            }
        }
    }

    private final List<Character> words = new LinkedList<>();

    private final String code;

    public Keyword(String keyword) {
        if (keyword.endsWith("\n")) {
            keyword = keyword.substring(0, keyword.length() - 1);
        }
        for (Character character : keyword.toCharArray()) {
            words.add(character);
        }
        this.code = Config.KEYWORD_PREFIX + Config.SPLIT_PREFIX + KEYWORDS.size();
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

package org.riseger.main.compiler.lextcal;

import lombok.Data;
import org.riseger.main.compiler.CompilerConstant;
import org.riseger.utils.tree.Equable;
import org.riseger.utils.tree.MultiTreeElement;

import java.util.LinkedList;
import java.util.List;

@Data
public class Keyword implements MultiTreeElement<Keyword> {
    //------------Static-------------//
    private static final List<Keyword> KEYWORDS = new LinkedList<>();
    private final int id;
    private final List<Character> words = new LinkedList<>();
    //------------Static-------------//
    private final String code;

    public Keyword(String keyword, int index) {
        if (keyword.endsWith("\n")) {
            keyword = keyword.substring(0, keyword.length() - 1);
        }
        for (Character character : keyword.toCharArray()) {
            words.add(character);
        }
        this.id = index;
        this.code = CompilerConstant.KEYWORD_PREFIX + CompilerConstant.SPLIT_PREFIX + KEYWORDS.size();
    }

    public static int addKeyword(String word) {
        Keyword keyword = new Keyword(word, KEYWORDS.size());
        KEYWORDS.add(keyword);
        return KEYWORDS.size() - 1;
    }

    public static List<Keyword> getKeywords() {
        return KEYWORDS;
    }

    public int size() {
        return words.size();
    }

    @Override
    public Equable next(int index) {
        if (this.words.size() == index) {
            return null;
        }
        return new C(this.words.get(index));
    }


    @Override
    public Keyword get() {
        return this;
    }


    @Override
    public boolean isTail(int index) {
        return this.words.size() == index + 1;
    }
}

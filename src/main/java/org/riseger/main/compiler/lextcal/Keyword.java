package org.riseger.main.compiler.lextcal;

import lombok.Data;
import org.riseger.main.compiler.CompilerConstant;
import org.riseger.utils.tree.Equable;
import org.riseger.utils.tree.MultiTreeElement;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Keyword implements MultiTreeElement<Keyword> {
    //------------Static-------------//
    private static final List<Keyword> KEYWORDS_LIST = new LinkedList<>();
    private static final Map<String, Integer> KEYWORDS_MAP_ = new HashMap<>();

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
        this.code = CompilerConstant.KEYWORD_PREFIX + CompilerConstant.SPLIT_PREFIX + index;
    }

    public static int addKeyword(String word) {
        if (KEYWORDS_MAP_.containsKey(word)) {
            return KEYWORDS_MAP_.get(word);
        } else {
            KEYWORDS_MAP_.put(word, KEYWORDS_MAP_.size());
            KEYWORDS_LIST.add(new Keyword(word, KEYWORDS_MAP_.size() - 1));
            return KEYWORDS_MAP_.size() - 1;
        }
    }

    public static List<Keyword> getKeywords() {
        return KEYWORDS_LIST;
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

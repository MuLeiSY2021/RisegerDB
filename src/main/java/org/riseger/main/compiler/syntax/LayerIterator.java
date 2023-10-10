package org.riseger.main.compiler.syntax;

import org.riseger.main.compiler.lextcal.Keyword;
import org.riseger.main.compiler.token.Token;

public class LayerIterator {
    private final SyntaxStructureTree.Node tmpParent;

    private int index = -1;

    public LayerIterator(SyntaxStructureTree.Node root) {
        this.tmpParent = root;
    }

    public LayerIterator deeper() {
        return new LayerIterator(tmpParent.get(index));
    }

    public void add(Token token, boolean isKeyword, Keyword keyword, int typeCode) {
        tmpParent.addFirst(token, isKeyword, keyword, typeCode);
        index++;
    }
}

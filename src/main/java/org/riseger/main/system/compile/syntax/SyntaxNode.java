package org.riseger.main.system.compile.syntax;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class SyntaxNode {
    private final List<List<Syntax>> metaList = new LinkedList<>();

    SyntaxNode(SyntaxRule.Rule entryRule, SyntaxRule rule) {
        for (SyntaxRule.Meta meta : entryRule.getMeta()) {
            List<Syntax> _tmp = new ArrayList<>();
            for (SyntaxRule.Tile tile : meta.getTiles()) {
                _tmp.add(new Syntax(rule, tile));
            }
            _tmp.get(_tmp.size() - 1).setFunctionFClass(meta.getFunctionClazz());
            metaList.add(_tmp);
        }
    }

}

package org.riseger.main.system.compile.syntax;

import lombok.Getter;
import org.riseger.main.system.compile.token.TokenType;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.utils.tree.MultiBranchesTree;

import java.util.HashMap;
import java.util.Map;

public class SyntaxForest {
    private final Map<Integer, MultiBranchesTree<Class<Function_f>>> forest = new HashMap<Integer, MultiBranchesTree<Class<Function_f>>>();

    private final Map<Integer, TokenType> finalTypeIdTable = new HashMap<>();
    @Getter
    private final int entry;
    @Getter
    private Class<Function_f> endFunctionClass;

    public SyntaxForest(SyntaxRule rule) throws Exception {
        this.entry = rule.getRuleMap().get("sql").getTypeId();

        for (Map.Entry<String, SyntaxRule.Rule> entry : rule.getRuleMap().entrySet()) {
            SyntaxRule.Rule entryRule = entry.getValue();
            if (entryRule.isEnd()) {
                finalTypeIdTable.put(entryRule.getTypeId(), TokenType.fromString(entry.getKey()));
                this.endFunctionClass = rule.getEndFunction();
            } else {
                MultiBranchesTree<Class<Function_f>> mbt = new MultiBranchesTree<>();
                for (SyntaxRule.Meta meta : entryRule.getMeta()) {
                    mbt.insert(new SyntaxTreeElement(meta, rule));
                }
                forest.put(entry.getValue().getTypeId(), mbt);
            }
        }
    }

    public MultiBranchesTree<Class<Function_f>> getSyntaxNode(int code) {
        return forest.get(code);
    }

    public boolean isEnd(int code) {
        return finalTypeIdTable.containsKey(code);
    }

    public boolean isEndType(int code, TokenType type) {
        return finalTypeIdTable.get(code).equals(type);
    }

    public TokenType getEndType(int code) {
        return finalTypeIdTable.get(code);
    }

    public boolean isHas(int code) {
        return forest.containsKey(code) || finalTypeIdTable.containsKey(code);
    }

}
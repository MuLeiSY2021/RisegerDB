package org.riseger.main.search;

import lombok.Data;
import org.riseger.main.search.function.Function_c;
import org.riseger.protoctl.search.command.WHERE;
import org.riseger.protoctl.search.function.FUNCTION;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SQLTree {
    private final SQLNode root;

    public SQLTree(WHERE where) {
        this.root = new SQLNode((FUNCTION) where.getCondition(), null);
    }

    public SQLTreeIterator iterator() {
        return new SQLTreeIterator(root, false);
    }

    public SQLTreeIterator submapIterator() {
        return new SQLTreeIterator(root, true);
    }

    @Data
    static class SQLNode {
        private final SQLNode parent;

        private Function_c function;

        private FUNCTION condition;

        private List<SQLNode> sqlList = new LinkedList<>();

        private boolean canSkip;

        public SQLNode(FUNCTION condition, SQLNode parent) {
            this.parent = parent;
            this.function = Function_c.getFunctionFromMap(condition);
            if (this.function != null) {
                this.function.setFunction(condition);
            }
            this.condition = condition;
            canSkip = condition.canSkip();
            if (condition.getFunctions() != null) {
                for (FUNCTION child : condition.getFunctions()) {
                    sqlList.add(new SQLNode(child, this));
                    canSkip |= child.canSkip();
                }
            }
            sqlList.sort(Comparator.comparing((SQLNode x) -> x.condition.getWeight()).reversed());
        }
    }
}

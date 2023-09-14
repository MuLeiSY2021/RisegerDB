package org.riseger.main.search;

import lombok.Data;
import org.riseger.protoctl.search.command.WHERE;
import org.riseger.protoctl.search.function.FUNCTION;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class SQLTree {
    private SQLNode root;

    public SQLTree(WHERE where) {
        this.root = new SQLNode((FUNCTION) where.getCondition(), null);
    }

    public SQLTreeIterator iterator() {
        return new SQLTreeIterator(root);
    }

    @Data
    static class SQLNode {
        private final SQLNode parent;

        private FUNCTION function;

        private List<SQLNode> sqlList = new LinkedList<>();


        public SQLNode(FUNCTION condition, SQLNode parent) {
            this.parent = parent;
            this.function = condition;
            for (FUNCTION child : function.getFunctions()) {
                sqlList.add(new SQLNode(child, this));
            }
            sqlList.sort(Comparator.comparing((SQLNode x) -> x.function.getWeight()).reversed());
        }
    }
}

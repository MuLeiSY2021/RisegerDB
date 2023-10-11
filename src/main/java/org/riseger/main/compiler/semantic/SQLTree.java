package org.riseger.main.compiler.semantic;

import lombok.Data;
import org.riseger.main.sql.function.type.BooleanFunction_c;
import org.riseger.main.sql.function.type.Function_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.command.WHERE;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.type.RECTANGLE_FUNCTIONBLE;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class SQLTree {
    private final SQLNode root;

    public SQLTree(WHERE where, SearchMemory searchMemory, double threshold) {
        this.root = new SQLNode((FUNCTION) where.getCondition(), null, searchMemory, threshold);
    }

    public SQLTree(RECTANGLE_FUNCTIONBLE function, SearchMemory searchMemory, double threshold) {
        this.root = new SQLNode((FUNCTION) function, null, searchMemory, threshold);
    }

    public Queue<Function_c<?>> genFunctionList() {
        Queue<Function_c<?>> queue = new LinkedBlockingQueue<>();
        walkTree(queue, this.root);
        return queue;
    }

    private void walkTree(Queue<Function_c<?>> queue, SQLNode root) {
        if (root.sqlList.isEmpty()) {
            queue.add(root.getFunction());
            return;
        } else {
            for (SQLNode node : root.sqlList) {
                walkTree(queue, node);
            }
        }
        queue.add(root.getFunction());
    }

    @Data
    static class SQLNode {
        private final SQLNode parent;

        private Function_c<?> function;

        private FUNCTION condition;

        private List<SQLNode> sqlList = new LinkedList<>();

        public SQLNode(FUNCTION condition, SQLNode parent, SearchMemory searchMemory, double threshold) {
            this.parent = parent;
            this.function = Function_c.getFunctionFromMap(condition, searchMemory, threshold);
            if (this.function != null) {
                this.function.setFunction(condition);
            }
            this.condition = condition;
            if (condition.getFunctions() != null) {
                for (FUNCTION child : condition.getFunctions()) {
                    sqlList.add(new SQLNode(child, this, searchMemory, threshold));
                }
            }
            if (isBool()) {
                sqlList.sort(Comparator.comparing((SQLNode x) -> x.condition.getWeight()).reversed());
            }
        }

        protected boolean isBool() {
            return function instanceof BooleanFunction_c;
        }
    }
}

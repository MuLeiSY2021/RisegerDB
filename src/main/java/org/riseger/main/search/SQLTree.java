package org.riseger.main.search;

import lombok.Data;
import org.riseger.main.search.function.type.BooleanFunction_c;
import org.riseger.main.search.function.type.Function_c;
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
        this.root = new SQLNode((FUNCTION) where.getCondition(), null,searchMemory, threshold);
    }

    public SQLTree(RECTANGLE_FUNCTIONBLE function, SearchMemory searchMemory, double threshold) {
        this.root = new SQLNode((FUNCTION) function, null,searchMemory, threshold);
    }

    @Data
    static class SQLNode {
        private final SQLNode parent;

        private Function_c<?> function;

        private FUNCTION condition;

        private List<SQLNode> sqlList = new LinkedList<>();

        private final int resultIndex;

        public SQLNode(FUNCTION condition, SQLNode parent, SearchMemory searchMemory, double threshold) {
            int index = 0;
            this.parent = parent;
            this.function = Function_c.getFunctionFromMap(condition,index,searchMemory,threshold);
            if (this.function != null) {
                this.function.setFunction(condition);
            }
            this.condition = condition;
            this.resultIndex = index;
            if (condition.getFunctions() != null) {
                for (FUNCTION child : condition.getFunctions()) {
                    sqlList.add(new SQLNode(child, this,++index,searchMemory, threshold));
                }
            }
            if(isBool()) {
                sqlList.sort(Comparator.comparing((SQLNode x) -> x.condition.getWeight()).reversed());
            }
        }

        public SQLNode(FUNCTION condition, SQLNode parent, int index, SearchMemory searchMemory, double threshold) {
            this.parent = parent;
            this.function = Function_c.getFunctionFromMap(condition,index,searchMemory,threshold);
            if (this.function != null) {
                this.function.setFunction(condition);
            }
            this.condition = condition;
            this.resultIndex = index;
            if (condition.getFunctions() != null) {
                for (FUNCTION child : condition.getFunctions()) {
                    sqlList.add(new SQLNode(child, this,index++,searchMemory, threshold));
                }
            }
            if(isBool()) {
                sqlList.sort(Comparator.comparing((SQLNode x) -> x.condition.getWeight()).reversed());
            }
        }

        protected boolean isBool() {
            return function instanceof BooleanFunction_c;
        }
    }

    public Queue<Function_c<?>> genFunctionList() {
        Queue<Function_c<?>> queue = new LinkedBlockingQueue<>();
        SQLNode tail = root;
        int index = 0;
        while (tail.getSqlList().size() != 0) {
            tail = tail.getSqlList().get(0);
        }

        while (tail.getParent() != null) {
            queue.add(tail.getFunction());
            index++;

            if (tail.getParent().getSqlList().size() <= index) {
                tail = tail.getParent();
                index = 0;
            }
            if(tail.getParent() != null) {
                tail = tail.getParent().getSqlList().get(index);
            }

        }
        queue.add(tail.getFunction());
        return queue;
    }
}

package org.riseger.main.sql.function.type;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.main.sql.function.entity.*;
import org.riseger.main.sql.function.graphic.In_fc;
import org.riseger.main.sql.function.graphic.Out_fc;
import org.riseger.main.sql.function.logic.And_fc;
import org.riseger.main.sql.function.logic.Not_fc;
import org.riseger.main.sql.function.logic.Or_fc;
import org.riseger.main.sql.function.math.*;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.condition.math.*;
import org.riseger.protoctl.search.function.entity.DISTANCE;
import org.riseger.protoctl.search.function.entity.NUMBER;
import org.riseger.protoctl.search.function.entity.basic.ATTRIBUTE;
import org.riseger.protoctl.search.function.entity.field.COORD;
import org.riseger.protoctl.search.function.entity.field.RECT;
import org.riseger.protoctl.search.function.key.graphic.IN;
import org.riseger.protoctl.search.function.key.graphic.OUT;
import org.riseger.protoctl.search.function.logic.AND;
import org.riseger.protoctl.search.function.logic.NOT;
import org.riseger.protoctl.search.function.logic.OR;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Getter
public abstract class Function_c {
    public static final Map<Class<? extends FUNCTION>, Class<? extends Function_c>>
            functionMap = new HashMap<>();
    protected static final Logger LOG = Logger.getLogger(Function_c.class);

    static {

        functionMap.put(ATTRIBUTE.class, Attribute_fc.class);
        functionMap.put(COORD.class, Coord_fc.class);
        functionMap.put(DISTANCE.class, Distance_fc.class);
        functionMap.put(NUMBER.class, Number_fc.class);
        functionMap.put(RECT.class, Rectangle_fc.class);

        functionMap.put(IN.class, In_fc.class);
        functionMap.put(OUT.class, Out_fc.class);

        functionMap.put(AND.class, And_fc.class);
        functionMap.put(NOT.class, Not_fc.class);
        functionMap.put(OR.class, Or_fc.class);

        functionMap.put(BIG.class, Big_fc.class);
        functionMap.put(BIG_EQUAL.class, BigEqual_fc.class);
        functionMap.put(EQUAL.class, Equal_fc.class);
        functionMap.put(SMALL.class, Small_fc.class);
        functionMap.put(SMALL_EQUAL.class, SmallEqual_fc.class);
    }

    protected final SearchMemory memory;

    private final double threshold;

    private final Queue<Function_c> functionQueue;


    public Function_c(FUNCTION function, SearchMemory memory, double threshold, Queue<Function_c> functionQueue) {
        this.memory = memory;
        this.threshold = threshold;
        this.functionQueue = functionQueue;
    }

    public static Function_c getFunctionFromMap(FUNCTION function, SearchMemory searchMemory, double threshold) {
        try {
            return functionMap.get(function.getClass())
                    .getConstructor(FUNCTION.class, SearchMemory.class, double.class)
                    .newInstance(function, searchMemory, threshold);
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
            LOG.debug(function.getClass());

        }
        return null;
    }

    protected Object get() {
        return memory.getVar();
    }

    protected void set(Object obj) {
        memory.setVar(obj);
    }

    public abstract void process();

    protected void jumpTo(int index) {
        //TODO:
    }
}

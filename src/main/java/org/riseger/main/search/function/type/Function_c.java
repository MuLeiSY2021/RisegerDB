package org.riseger.main.search.function.type;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.exception.search.function.IllegalSearchAttributeException;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.entity.*;
import org.riseger.main.search.function.graphic.In_fc;
import org.riseger.main.search.function.graphic.Out_fc;
import org.riseger.main.search.function.logic.And_fc;
import org.riseger.main.search.function.logic.Not_fc;
import org.riseger.main.search.function.logic.Or_fc;
import org.riseger.main.search.function.math.*;
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

@Getter
public abstract class Function_c<R> {
    protected static final Logger LOG = Logger.getLogger(Function_c.class);

    private final SearchMemory memory;

    private final double threshold;

    public static final Map<Class<? extends FUNCTION>, Class<? extends Function_c<?>>>
            functionMap = new HashMap<>();
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


    public Function_c(SearchMemory memory, double threshold) {
        this.memory = memory;
        this.threshold = threshold;
    }

    public static Function_c<?> getFunctionFromMap(FUNCTION function, SearchMemory searchMemory, double threshold) {
        try {
            return functionMap.get(function.getClass())
                    .getConstructor(SearchMemory.class, double.class)
                    .newInstance(searchMemory, threshold);
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
            LOG.debug(function.getClass());

        }
        return null;
    }

    //这个函数多少有点多余了........
    public abstract void setFunction(FUNCTION condition);


    public abstract R resolve(Element_c element) throws IllegalSearchAttributeException;

    protected Object get() {
        return memory.get();
    }

    protected void set(Object obj) {
        memory.set(obj);
    }
}

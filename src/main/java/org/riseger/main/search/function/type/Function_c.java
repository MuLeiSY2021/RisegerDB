package org.riseger.main.search.function.type;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.Element_c;
import org.riseger.main.search.SearchMemory;
import org.riseger.main.search.function.math.*;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.condition.math.*;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Function_c<R> {
    private static final Logger LOG = Logger.getLogger(Function_c.class);

    private final int indexStart;

    private final SearchMemory memory;

    public static final Map<Class<? extends FUNCTION>, Class<? extends Function_c<?>>>
            functionMap = new HashMap<>();
    static {
        functionMap.put(BIG.class,Big_fc.class);
        functionMap.put(BIG_EQUAL.class, BigEqual_fc.class);
        functionMap.put(EQUAL.class, Equal_fc.class);
        functionMap.put(SMALL.class, Small_fc.class);
        functionMap.put(SMALL_EQUAL.class, SmallEqual_fc.class);

    }


    public Function_c(int indexStart, SearchMemory memory) {
        this.indexStart = indexStart;
        this.memory = memory;
    }

    public static Function_c<?> getFunctionFromMap(FUNCTION function,int indexStart,SearchMemory searchMemory) {
        try {
            return functionMap.get(function.getClass()).getConstructor(Integer.class, SearchMemory.class).newInstance(indexStart,searchMemory);
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        }
        return null;
    }

    //这个函数多少有点多余了........
    public abstract void setFunction(FUNCTION condition);


    public abstract R getResult(Element_c element);

    protected Object get(int gap) {
        return memory.get(indexStart + gap);
    }

    protected void set(Object obj) {
        memory.set(indexStart,obj);
    }
}

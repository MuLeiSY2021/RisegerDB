package org.riseger.main.search.function.type;

import org.apache.log4j.Logger;
import org.riseger.main.cache.entity.component.MBRectangle_c;
import org.riseger.main.cache.manager.ElementManager;
import org.riseger.protoctl.search.function.FUNCTION;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Function_c {
    public static final Map<Constructor<? extends Function_c>, Class<? extends Function_c>> functionMap = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(Function_c.class);

    static {
        //TODO: 还差所有这些函数的逻辑
    }

    private final Class<? extends FUNCTION> parent;

    public Function_c(Class<? extends FUNCTION> parent) {
        this.parent = parent;
    }

    public static Function_c getFunctionFromMap(FUNCTION function) {
        try {
            return null;
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        }
        return null;
    }

    public abstract List<MBRectangle_c> getResult(ElementManager elements);

    public abstract void setFunction(FUNCTION condition);

    public abstract boolean isResult(MBRectangle_c mb);
}

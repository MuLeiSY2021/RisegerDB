package org.riseger.protoctl.search.function;

import org.apache.log4j.Logger;
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
import org.riseger.protoctl.search.function.type.FUNCTIONAL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FUNCTION implements FUNCTIONAL {

    public static final Map<Integer, Class<? extends FUNCTIONAL>> idMap = new HashMap<>();

    private static final Map<Class<? extends FUNCTIONAL>, Integer> classMap = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(FUNCTION.class);

    static {
        FUNCTION.set(ATTRIBUTE.class);
        FUNCTION.set(COORD.class);
        FUNCTION.set(RECT.class);
        FUNCTION.set(DISTANCE.class);
        FUNCTION.set(IN.class);
        FUNCTION.set(OUT.class);
        FUNCTION.set(AND.class);
        FUNCTION.set(NOT.class);
        FUNCTION.set(OR.class);
        FUNCTION.set(BIG.class);
        FUNCTION.set(BIG_EQUAL.class);
        FUNCTION.set(EQUAL.class);
        FUNCTION.set(NUMBER.class);
        FUNCTION.set(SMALL.class);
        FUNCTION.set(SMALL_EQUAL.class);
    }

    private final int functionId;

    public FUNCTION(Class<? extends FUNCTIONAL> clazz) {
        this.functionId = classMap.get(clazz);
    }

    public static void set(Class<? extends FUNCTIONAL> clazz) {
        set(clazz.getName());
    }

    public static void set(String className) {
        int tmpid = className.hashCode();
        while (FUNCTION.idMap.containsKey(tmpid)) {
            tmpid++;
        }
        try {
            Class<? extends FUNCTIONAL> clazz = (Class<? extends FUNCTIONAL>) Class.forName(className);
            idMap.put(tmpid, clazz);
            classMap.put(clazz, tmpid);
        } catch (ClassNotFoundException e) {
            LOG.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public int getId() {
        return functionId;
    }

    public abstract List<FUNCTION> getFunctions();

    public abstract Integer getWeight();


}

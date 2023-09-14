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
import org.riseger.protoctl.search.function.type.FUNCTIONBLE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FUNCTION implements FUNCTIONBLE {

    public static final Map<Integer, Class<? extends FUNCTIONBLE>> idMap = new HashMap<>();

    private static final Map<Class<? extends FUNCTIONBLE>, Integer> classMap = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(FUNCTION.class);

    static {
        FUNCTION.getId(ATTRIBUTE.class);
        FUNCTION.getId(COORD.class);
        FUNCTION.getId(RECT.class);
        FUNCTION.getId(DISTANCE.class);
        FUNCTION.getId(IN.class);
        FUNCTION.getId(OUT.class);
        FUNCTION.getId(AND.class);
        FUNCTION.getId(NOT.class);
        FUNCTION.getId(OR.class);
        FUNCTION.getId(BIG.class);
        FUNCTION.getId(BIG_EQUAL.class);
        FUNCTION.getId(EQUAL.class);
        FUNCTION.getId(NUMBER.class);
        FUNCTION.getId(SMALL.class);
        FUNCTION.getId(SMALL_EQUAL.class);
    }

    private final int functionId;

    public FUNCTION(Class<? extends FUNCTIONBLE> clazz) {
        this.functionId = classMap.get(clazz);
    }

    public static int getId(Class<? extends FUNCTIONBLE> clazz) {
        return getId(clazz.getName());
    }

    public static int getId(String className) {
        int tmpid = className.hashCode();
        while (FUNCTION.idMap.containsKey(tmpid)) {
            tmpid++;
        }
        try {
            Class<? extends FUNCTIONBLE> clazz = (Class<? extends FUNCTIONBLE>) Class.forName(className);
            idMap.put(tmpid, clazz);
            classMap.put(clazz, tmpid);
        } catch (ClassNotFoundException e) {
            LOG.error(e);
            e.printStackTrace();
        }
        return tmpid;
    }

    @Override
    public int getId() {
        return functionId;
    }

    public abstract List<FUNCTION> getFunctions();

    public abstract Integer getWeight();
}

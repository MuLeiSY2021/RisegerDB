package org.riseger.protoctl.search.function;

import org.apache.log4j.Logger;
import org.riseger.protoctl.search.function.entity.Attribute_f;
import org.riseger.protoctl.search.function.entity.Coord_f;
import org.riseger.protoctl.search.function.entity.Distance_f;
import org.riseger.protoctl.search.function.entity.Rect_f;
import org.riseger.protoctl.search.function.graphic.In_f;
import org.riseger.protoctl.search.function.graphic.Out_f;
import org.riseger.protoctl.search.function.logic.And_f;
import org.riseger.protoctl.search.function.logic.Not_f;
import org.riseger.protoctl.search.function.logic.Or_f;
import org.riseger.protoctl.search.function.math.*;
import org.riseger.protoctl.search.function.type.FUNCTIONAL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Function_f implements FUNCTIONAL {

    public static final Map<Integer, Class<? extends FUNCTIONAL>> idMap = new HashMap<>();

    private static final Map<Class<? extends FUNCTIONAL>, Integer> classMap = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(Function_f.class);

    static {
        Function_f.set(Attribute_f.class);
        Function_f.set(Coord_f.class);
        Function_f.set(Rect_f.class);
        Function_f.set(Distance_f.class);
        Function_f.set(In_f.class);
        Function_f.set(Out_f.class);
        Function_f.set(And_f.class);
        Function_f.set(Not_f.class);
        Function_f.set(Or_f.class);
        Function_f.set(Big_f.class);
        Function_f.set(BigEqual_f.class);
        Function_f.set(Equal_f.class);
        Function_f.set(Small_f.class);
        Function_f.set(SmallEqual_f.class);
    }

    private final int functionId;

    public Function_f(Class<? extends FUNCTIONAL> clazz) {
        this.functionId = classMap.get(clazz);
    }

    public static void set(Class<? extends FUNCTIONAL> clazz) {
        set(clazz.getName());
    }

    public static void set(String className) {
        int tmpid = className.hashCode();
        while (Function_f.idMap.containsKey(tmpid)) {
            tmpid++;
        }
        try {
            Class<? extends FUNCTIONAL> clazz = (Class<? extends FUNCTIONAL>) Class.forName(className);
            idMap.put(tmpid, clazz);
            classMap.put(clazz, tmpid);
        } catch (ClassNotFoundException e) {
            LOG.error("Error ", e);
        }
    }

    @Override
    public int getId() {
        return functionId;
    }

    public abstract List<Function_f> getFunctions();

    public abstract Integer getWeight();


}

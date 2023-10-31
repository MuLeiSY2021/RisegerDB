package org.riseger.protoctl.compiler.function;

import org.apache.log4j.Logger;
import org.riseger.protoctl.compiler.function.entity.Attribute_f;
import org.riseger.protoctl.compiler.function.entity.Coord_f;
import org.riseger.protoctl.compiler.function.entity.Distance_f;
import org.riseger.protoctl.compiler.function.entity.Rectangle_f;
import org.riseger.protoctl.compiler.function.graphic.In_f;
import org.riseger.protoctl.compiler.function.graphic.Out_f;
import org.riseger.protoctl.compiler.function.logic.And_f;
import org.riseger.protoctl.compiler.function.logic.Not_f;
import org.riseger.protoctl.compiler.function.logic.Or_f;
import org.riseger.protoctl.compiler.function.math.*;
import org.riseger.protoctl.compiler.function.type.Functional;

import java.util.HashMap;
import java.util.Map;

public abstract class Function_f implements Functional {

    public static final Map<Integer, Class<? extends Functional>> idMap = new HashMap<>();

    private static final Map<Class<? extends Functional>, Integer> classMap = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(Function_f.class);

    static {
        Function_f.set(Attribute_f.class);
        Function_f.set(Coord_f.class);
        Function_f.set(Rectangle_f.class);
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

    public Function_f(Class<? extends Functional> clazz) {
        this.functionId = classMap.get(clazz);
    }

    public static void set(Class<? extends Functional> clazz) {
        set(clazz.getName());
    }

    public static void set(String className) {
        int tmpid = className.hashCode();
        while (Function_f.idMap.containsKey(tmpid)) {
            tmpid++;
        }
        try {
            Class<? extends Functional> clazz = (Class<? extends Functional>) Class.forName(className);
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

    public abstract Integer getWeight();


}

package org.riseger.protoctl.search.function;

import org.apache.log4j.Logger;
import org.riseger.protoctl.search.function.condition.math.*;
import org.riseger.protoctl.search.function.entity.Attribute_F;
import org.riseger.protoctl.search.function.entity.Coord_F;
import org.riseger.protoctl.search.function.entity.Distance_f;
import org.riseger.protoctl.search.function.entity.Rect_F;
import org.riseger.protoctl.search.function.key.graphic.In_F;
import org.riseger.protoctl.search.function.key.graphic.Out_F;
import org.riseger.protoctl.search.function.logic.And_F;
import org.riseger.protoctl.search.function.logic.Not_F;
import org.riseger.protoctl.search.function.logic.Or_F;
import org.riseger.protoctl.search.function.type.FUNCTIONAL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Function_F implements FUNCTIONAL {

    public static final Map<Integer, Class<? extends FUNCTIONAL>> idMap = new HashMap<>();

    private static final Map<Class<? extends FUNCTIONAL>, Integer> classMap = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(Function_F.class);

    static {
        Function_F.set(Attribute_F.class);
        Function_F.set(Coord_F.class);
        Function_F.set(Rect_F.class);
        Function_F.set(Distance_f.class);
        Function_F.set(In_F.class);
        Function_F.set(Out_F.class);
        Function_F.set(And_F.class);
        Function_F.set(Not_F.class);
        Function_F.set(Or_F.class);
        Function_F.set(Big_F.class);
        Function_F.set(BigEqual_F.class);
        Function_F.set(Equal_F.class);
        Function_F.set(Small_F.class);
        Function_F.set(SmallEqual_F.class);
    }

    private final int functionId;

    public Function_F(Class<? extends FUNCTIONAL> clazz) {
        this.functionId = classMap.get(clazz);
    }

    public static void set(Class<? extends FUNCTIONAL> clazz) {
        set(clazz.getName());
    }

    public static void set(String className) {
        int tmpid = className.hashCode();
        while (Function_F.idMap.containsKey(tmpid)) {
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

    public abstract List<Function_F> getFunctions();

    public abstract Integer getWeight();


}

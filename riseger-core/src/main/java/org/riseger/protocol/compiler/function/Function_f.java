package org.riseger.protocol.compiler.function;

import org.apache.log4j.Logger;
import org.riseger.protocol.compiler.function.entity.*;
import org.riseger.protocol.compiler.function.get.GetDatabases_f;
import org.riseger.protocol.compiler.function.get.GetMaps_f;
import org.riseger.protocol.compiler.function.get.GetModels_f;
import org.riseger.protocol.compiler.function.graphic.In_f;
import org.riseger.protocol.compiler.function.graphic.Out_f;
import org.riseger.protocol.compiler.function.logic.And_f;
import org.riseger.protocol.compiler.function.logic.Not_f;
import org.riseger.protocol.compiler.function.logic.Or_f;
import org.riseger.protocol.compiler.function.loop.Back_f;
import org.riseger.protocol.compiler.function.loop.IfJump_Pass_f;
import org.riseger.protocol.compiler.function.loop.IfJump_f;
import org.riseger.protocol.compiler.function.main.*;
import org.riseger.protocol.compiler.function.math.*;
import org.riseger.protocol.compiler.function.number.*;
import org.riseger.protocol.compiler.function.preload.Preload_f;
import org.riseger.protocol.compiler.function.type.Functional;

import java.util.HashMap;
import java.util.Map;

public abstract class Function_f implements Functional {

    public static final Map<Integer, Class<? extends Functional>> idMap = new HashMap<>();

    private static final Logger LOG = Logger.getLogger(Function_f.class);

    private static final Map<Class<? extends Functional>, Integer> classMap = new HashMap<>();
    static {
        Function_f.set(Attribute_f.class);
        Function_f.set(Coord_f.class);
        Function_f.set(Distance_f.class);
        Function_f.set(Rectangle_f.class);
        Function_f.set(Strings_f.class);
        Function_f.set(TopStrings_f.class);

        Function_f.set(In_f.class);
        Function_f.set(Out_f.class);

        Function_f.set(And_f.class);
        Function_f.set(Not_f.class);
        Function_f.set(Or_f.class);

        Function_f.set(Back_f.class);
        Function_f.set(IfJump_f.class);
        Function_f.set(IfJump_Pass_f.class);

        Function_f.set(Search_f.class);
        Function_f.set(UseDatabase_f.class);
        Function_f.set(UseMap_f.class);
        Function_f.set(UseModel_f.class);
        Function_f.set(UseScope_f.class);
        Function_f.set(Where_f.class);
        Function_f.set(PreWhere_f.class);
        Function_f.set(PostWhere_f.class);

        Function_f.set(Big_f.class);
        Function_f.set(BigEqual_f.class);
        Function_f.set(Equal_f.class);
        Function_f.set(Small_f.class);
        Function_f.set(SmallEqual_f.class);

        //Number
        Function_f.set(AddNumber_f.class);
        Function_f.set(DivideNumber_f.class);
        Function_f.set(MutiNumber_f.class);
        Function_f.set(NegivateNumber_f.class);
        Function_f.set(SubNumber_f.class);

        Function_f.set(Entity_f.class);

        Function_f.set(GetDatabases_f.class);
        Function_f.set(GetMaps_f.class);
        Function_f.set(GetModels_f.class);

        Function_f.set(Preload_f.class);

        Function_f.set(DotStrings_f.class);
        Function_f.set(TopDotStrings_f.class);

        Function_f.set(Coords_f.class);
        Function_f.set(TopCoords_f.class);

        Function_f.set(CoordToRect_f.class);

        Function_f.set(Update_f.class);
    }

    private final int functionId;

    public Function_f(Class<? extends Functional> clazz) {
        try {
            this.functionId = Function_f.classMap.get(clazz);
        } catch (Exception e) {
            LOG.debug("函数ID未找到，类型名为：" + clazz.getCanonicalName());
            LOG.error("函数ID未找到，类型名为：" + clazz.getCanonicalName(), e);
            throw e;
        }
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

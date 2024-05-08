package org.riseger.main.system.compile.function;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.entity.*;
import org.riseger.main.system.compile.function.get.GetDatabases_fc;
import org.riseger.main.system.compile.function.get.GetMaps_fc;
import org.riseger.main.system.compile.function.get.GetModels_fc;
import org.riseger.main.system.compile.function.graphic.In_fc;
import org.riseger.main.system.compile.function.graphic.Out_fc;
import org.riseger.main.system.compile.function.logic.And_fc;
import org.riseger.main.system.compile.function.logic.Not_fc;
import org.riseger.main.system.compile.function.logic.Or_fc;
import org.riseger.main.system.compile.function.loop.Back_fc;
import org.riseger.main.system.compile.function.loop.IfJump_Pass_fc;
import org.riseger.main.system.compile.function.loop.IfJump_fc;
import org.riseger.main.system.compile.function.main.*;
import org.riseger.main.system.compile.function.math.*;
import org.riseger.main.system.compile.function.number.*;
import org.riseger.main.system.compile.function.preload.Preload_fc;
import org.riseger.protocol.compiler.function.Entity_f;
import org.riseger.protocol.compiler.function.Function_f;
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
import org.riseger.protocol.exception.SQLException;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Function_c {
    public static final Map<Class<? extends Function_f>, Class<? extends Function_c>>
            functionMap = new HashMap<>();

    protected static final Logger LOG = Logger.getLogger(Function_c.class);

    static {

        functionMap.put(Attribute_f.class, Attribute_fc.class);
        functionMap.put(Coord_f.class, Coord_fc.class);
        functionMap.put(Distance_f.class, Distance_fc.class);
        functionMap.put(Rectangle_f.class, Rectangle_fc.class);
        functionMap.put(Strings_f.class, Strings_fc.class);
        functionMap.put(TopStrings_f.class, TopStrings_fc.class);

        functionMap.put(In_f.class, In_fc.class);
        functionMap.put(Out_f.class, Out_fc.class);

        functionMap.put(And_f.class, And_fc.class);
        functionMap.put(Not_f.class, Not_fc.class);
        functionMap.put(Or_f.class, Or_fc.class);

        functionMap.put(Back_f.class, Back_fc.class);
        functionMap.put(IfJump_f.class, IfJump_fc.class);
        functionMap.put(IfJump_Pass_f.class, IfJump_Pass_fc.class);

        functionMap.put(Search_f.class, Search_fc.class);
        functionMap.put(UseDatabase_f.class, UseDatabase_fc.class);
        functionMap.put(UseMap_f.class, UseMap_fc.class);
        functionMap.put(UseModel_f.class, UseModel_fc.class);
        functionMap.put(UseScope_f.class, UseScope_fc.class);
        functionMap.put(Where_f.class, Where_fc.class);
        functionMap.put(PreWhere_f.class, PreWhere_fc.class);
        functionMap.put(PostWhere_f.class, PostWhere_fc.class);

        functionMap.put(Big_f.class, Big_fc.class);
        functionMap.put(BigEqual_f.class, BigEqual_fc.class);
        functionMap.put(Equal_f.class, Equal_fc.class);
        functionMap.put(Small_f.class, Small_fc.class);
        functionMap.put(SmallEqual_f.class, SmallEqual_fc.class);

        //Number
        functionMap.put(AddNumber_f.class, AddNumber_fc.class);
        functionMap.put(DivideNumber_f.class, DivideNumber_fc.class);
        functionMap.put(MutiNumber_f.class, MutiNumber_fc.class);
        functionMap.put(NegivateNumber_f.class, NegivateNumber_fc.class);
        functionMap.put(SubNumber_f.class, SubNumber_fc.class);

        functionMap.put(Entity_f.class, Entity_fc.class);

        functionMap.put(GetDatabases_f.class, GetDatabases_fc.class);
        functionMap.put(GetMaps_f.class, GetMaps_fc.class);
        functionMap.put(GetModels_f.class, GetModels_fc.class);

        functionMap.put(Preload_f.class, Preload_fc.class);

        //New
        functionMap.put(DotStrings_f.class, DotStrings_fc.class);
        functionMap.put(TopDotStrings_f.class, TopDotStrings_fc.class);

        functionMap.put(Coords_f.class, Coords_fc.class);
        functionMap.put(TopCoords_f.class, TopCoords_fc.class);

        functionMap.put(CoordToRect_f.class, CoordToRect_fc.class);
        functionMap.put(WriteUpdateLog_f.class, WiteUpdateLog_fc.class);

        functionMap.put(Update_f.class, Update_fc.class);

    }

    public Function_c() {
    }

    public static Function_c getFunctionFromMap(Function_f function, SearchMemory searchMemory, CommandList commandList) {
        try {
            if (function instanceof Entity_f) {
                return functionMap.get(function.getClass())
                        .getConstructor(Object.class)
                        .newInstance(((Entity_f) function).getEntity());
            }
            return functionMap.get(function.getClass())
                    .getConstructor()
                    .newInstance();
        } catch (Exception e) {
            LOG.error("Error ", e);
            LOG.debug(function.getClass());

        }
        return null;
    }

    public abstract void process(SearchMemory searchMemory, CommandList commandList) throws SQLException;

}

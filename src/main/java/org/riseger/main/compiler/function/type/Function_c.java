package org.riseger.main.compiler.function.type;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.MemoryConstant;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.entity.*;
import org.riseger.main.compiler.function.graphic.In_fc;
import org.riseger.main.compiler.function.graphic.Out_fc;
import org.riseger.main.compiler.function.logic.And_fc;
import org.riseger.main.compiler.function.logic.Not_fc;
import org.riseger.main.compiler.function.logic.Or_fc;
import org.riseger.main.compiler.function.main.*;
import org.riseger.main.compiler.function.math.*;
import org.riseger.main.compiler.function.number.*;
import org.riseger.protoctl.compiler.function.Entity_f;
import org.riseger.protoctl.compiler.function.Function_f;
import org.riseger.protoctl.compiler.function.entity.*;
import org.riseger.protoctl.compiler.function.graphic.In_f;
import org.riseger.protoctl.compiler.function.graphic.Out_f;
import org.riseger.protoctl.compiler.function.logic.And_f;
import org.riseger.protoctl.compiler.function.logic.Not_f;
import org.riseger.protoctl.compiler.function.logic.Or_f;
import org.riseger.protoctl.compiler.function.main.*;
import org.riseger.protoctl.compiler.function.math.*;
import org.riseger.protoctl.compiler.number.*;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

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

        functionMap.put(In_f.class, In_fc.class);
        functionMap.put(Out_f.class, Out_fc.class);

        functionMap.put(And_f.class, And_fc.class);
        functionMap.put(Not_f.class, Not_fc.class);
        functionMap.put(Or_f.class, Or_fc.class);

        functionMap.put(Search_f.class, Search_fc.class);
        functionMap.put(UseDatabase_f.class, UseDatabase_fc.class);
        functionMap.put(UseMap_f.class, UseMap_fc.class);
        functionMap.put(UseModel_f.class, UseModel_fc.class);
        functionMap.put(UseScope_f.class, UseScope_fc.class);
        functionMap.put(Where_f.class, Where_fc.class);

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

        functionMap.put(Entity_f.class, Equal_fc.class);
    }

    private final SearchMemory memory;

    private final CommandList commandList;


    public Function_c(SearchMemory memory, CommandList commandList) {
        this.memory = memory;
        this.commandList = commandList;
    }

    public static Function_c getFunctionFromMap(Function_f function, SearchMemory searchMemory, CommandList commandList) {
        try {
            return functionMap.get(function.getClass())
                    .getConstructor(SearchMemory.class, CommandList.class)
                    .newInstance(searchMemory, commandList);
        } catch (Exception e) {
            LOG.error("Error ", e);
            LOG.debug(function.getClass());

        }
        return null;
    }

    protected Object poll() {
        return memory.getVar();
    }

    protected void put(Object obj) {
        memory.setVar(obj);
    }

    protected Object getMap(MemoryConstant constant) {
        return memory.getMapValue(constant);
    }

    protected void setMap(Object o, MemoryConstant constant) {
        this.memory.setMapValue(o, constant);
    }

    public abstract void process() throws IllegalSearchAttributeException;

    protected void jumpTo(int index) {
        commandList.jump(index);
    }

    protected int index() {
        return commandList.index();
    }

    protected boolean hasMap(MemoryConstant constant) {
        return this.memory.hasMapValue(constant);
    }

}

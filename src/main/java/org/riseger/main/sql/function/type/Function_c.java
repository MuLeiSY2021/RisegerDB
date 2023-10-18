package org.riseger.main.sql.function.type;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.main.sql.compoent.CommandList;
import org.riseger.main.sql.compoent.MemoryConstant;
import org.riseger.main.sql.compoent.SearchMemory;
import org.riseger.main.sql.function.entity.Attribute_fc;
import org.riseger.main.sql.function.entity.Coord_fc;
import org.riseger.main.sql.function.entity.Distance_fc;
import org.riseger.main.sql.function.entity.Rectangle_fc;
import org.riseger.main.sql.function.graphic.In_fc;
import org.riseger.main.sql.function.graphic.Out_fc;
import org.riseger.main.sql.function.logic.And_fc;
import org.riseger.main.sql.function.logic.Not_fc;
import org.riseger.main.sql.function.logic.Or_fc;
import org.riseger.main.sql.function.math.*;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;
import org.riseger.protoctl.search.function.FUNCTION;
import org.riseger.protoctl.search.function.condition.math.*;
import org.riseger.protoctl.search.function.entity.*;
import org.riseger.protoctl.search.function.key.graphic.IN;
import org.riseger.protoctl.search.function.key.graphic.OUT;
import org.riseger.protoctl.search.function.logic.AND;
import org.riseger.protoctl.search.function.logic.NOT;
import org.riseger.protoctl.search.function.logic.OR;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Function_c {
    public static final Map<Class<? extends FUNCTION>, Class<? extends Function_c>>
            functionMap = new HashMap<>();
    protected static final Logger LOG = Logger.getLogger(Function_c.class);

    static {

        functionMap.put(Attribute_F.class, Attribute_fc.class);
        functionMap.put(COORD.class, Coord_fc.class);
        functionMap.put(DISTANCE.class, Distance_fc.class);
        functionMap.put(NUMBER.class, Number_fc.class);
        functionMap.put(RECT.class, Rectangle_fc.class);

        functionMap.put(IN.class, In_fc.class);
        functionMap.put(OUT.class, Out_fc.class);

        functionMap.put(AND.class, And_fc.class);
        functionMap.put(NOT.class, Not_fc.class);
        functionMap.put(OR.class, Or_fc.class);

        functionMap.put(Big.class, Big_fc.class);
        functionMap.put(BigEqual.class, BigEqual_fc.class);
        functionMap.put(Equal.class, Equal_fc.class);
        functionMap.put(Small.class, Small_fc.class);
        functionMap.put(SmallEqual.class, SmallEqual_fc.class);
    }

    private final SearchMemory memory;

    private final double threshold;

    private final CommandList commandList;


    public Function_c(FUNCTION function, SearchMemory memory, double threshold, CommandList commandList) {
        this.memory = memory;
        this.threshold = threshold;
        this.commandList = commandList;
    }

    public static Function_c getFunctionFromMap(FUNCTION function, SearchMemory searchMemory, double threshold) {
        try {
            return functionMap.get(function.getClass())
                    .getConstructor(FUNCTION.class, SearchMemory.class, double.class)
                    .newInstance(function, searchMemory, threshold);
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
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

    protected Object getConstant(int index) {
        return this.memory.getConstant(index);
    }

    public abstract void process() throws IllegalSearchAttributeException;

    protected void jumpTo(int index) {
        commandList.jump(index);
    }

    protected int index() {
        return commandList.index();
    }
}

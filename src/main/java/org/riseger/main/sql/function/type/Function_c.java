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
import org.riseger.protoctl.search.function.Function_f;
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
        functionMap.put(Rect_f.class, Rectangle_fc.class);

        functionMap.put(In_f.class, In_fc.class);
        functionMap.put(Out_f.class, Out_fc.class);

        functionMap.put(And_f.class, And_fc.class);
        functionMap.put(Not_f.class, Not_fc.class);
        functionMap.put(Or_f.class, Or_fc.class);

        functionMap.put(Big_f.class, Big_fc.class);
        functionMap.put(BigEqual_f.class, BigEqual_fc.class);
        functionMap.put(Equal_f.class, Equal_fc.class);
        functionMap.put(Small_f.class, Small_fc.class);
        functionMap.put(SmallEqual_f.class, SmallEqual_fc.class);
    }

    private final SearchMemory memory;

    private final double threshold;

    private final CommandList commandList;


    public Function_c(SearchMemory memory, double threshold, CommandList commandList) {
        this.memory = memory;
        this.threshold = threshold;
        this.commandList = commandList;
    }

    public static Function_c getFunctionFromMap(Function_f function, SearchMemory searchMemory, double threshold) {
        try {
            return functionMap.get(function.getClass())
                    .getConstructor(Function_f.class, SearchMemory.class, double.class)
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

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
import org.riseger.protoctl.search.function.Function_F;
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

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Function_c {
    public static final Map<Class<? extends Function_F>, Class<? extends Function_c>>
            functionMap = new HashMap<>();
    protected static final Logger LOG = Logger.getLogger(Function_c.class);

    static {

        functionMap.put(Attribute_F.class, Attribute_fc.class);
        functionMap.put(Coord_F.class, Coord_fc.class);
        functionMap.put(Distance_f.class, Distance_fc.class);
        functionMap.put(Rect_F.class, Rectangle_fc.class);

        functionMap.put(In_F.class, In_fc.class);
        functionMap.put(Out_F.class, Out_fc.class);

        functionMap.put(And_F.class, And_fc.class);
        functionMap.put(Not_F.class, Not_fc.class);
        functionMap.put(Or_F.class, Or_fc.class);

        functionMap.put(Big_F.class, Big_fc.class);
        functionMap.put(BigEqual_F.class, BigEqual_fc.class);
        functionMap.put(Equal_F.class, Equal_fc.class);
        functionMap.put(Small_F.class, Small_fc.class);
        functionMap.put(SmallEqual_F.class, SmallEqual_fc.class);
    }

    private final SearchMemory memory;

    private final double threshold;

    private final CommandList commandList;


    public Function_c(SearchMemory memory, double threshold, CommandList commandList) {
        this.memory = memory;
        this.threshold = threshold;
        this.commandList = commandList;
    }

    public static Function_c getFunctionFromMap(Function_F function, SearchMemory searchMemory, double threshold) {
        try {
            return functionMap.get(function.getClass())
                    .getConstructor(Function_F.class, SearchMemory.class, double.class)
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

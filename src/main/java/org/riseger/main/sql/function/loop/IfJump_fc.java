package org.riseger.main.sql.function.loop;

import org.riseger.main.sql.function.type.LoopFunction_c;
import org.riseger.main.sql.search.SearchMemory;
import org.riseger.protoctl.search.function.FUNCTION;

public class IfJump_fc extends LoopFunction_c {
    public IfJump_fc(FUNCTION function, SearchMemory memory, double threshold) {
        super(function, memory, threshold);
    }
}

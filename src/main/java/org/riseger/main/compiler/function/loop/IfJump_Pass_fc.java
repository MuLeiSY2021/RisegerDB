package org.riseger.main.compiler.function.loop;

import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.LoopFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class IfJump_Pass_fc extends LoopFunction_c {
    private static final Logger LOG = Logger.getLogger(IfJump_Pass_fc.class);

    public IfJump_Pass_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        int index = (Integer) super.poll();
        boolean result = (boolean) super.poll();
        if (result) {
            LOG.debug("满足条件，跳转至:" + index);
            super.jumpTo(index);
        }
    }

}

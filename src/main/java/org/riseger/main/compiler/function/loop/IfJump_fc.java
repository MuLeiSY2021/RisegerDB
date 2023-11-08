package org.riseger.main.compiler.function.loop;

import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.LoopFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class IfJump_fc extends LoopFunction_c {
    private static final Logger LOG = Logger.getLogger(IfJump_fc.class);

    public IfJump_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        int index = (Integer) super.poll();
        boolean result = (boolean) super.poll();
        if (result) {
            LOG.debug("满足条件，跳转至:" + index);
            super.jumpTo(index);
        } else {
            LOG.debug("不满足条件，放回");
            super.put(false);
        }
    }

}

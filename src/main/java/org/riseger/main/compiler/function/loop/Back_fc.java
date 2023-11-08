package org.riseger.main.compiler.function.loop;

import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.LoopFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Back_fc extends LoopFunction_c {
    private static final Logger LOG = Logger.getLogger(Back_fc.class);

    public Back_fc(SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        int index = (int) super.poll();
        LOG.debug("满足条件，跳转至:" + index);

        super.jumpTo(index);
    }
}

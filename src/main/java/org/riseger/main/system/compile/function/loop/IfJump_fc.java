package org.riseger.main.system.compile.function.loop;

import org.apache.log4j.Logger;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class IfJump_fc extends Function_c {
    private static final Logger LOG = Logger.getLogger(IfJump_fc.class);

    public IfJump_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        int index = (Integer) searchMemory.poll();
        boolean result = (boolean) searchMemory.poll();
        if (result) {
            LOG.debug("满足条件，跳转至:" + index);
            commandList.jumpTo(index);
        } else {
            LOG.debug("不满足条件，放回");
            searchMemory.push(false);
        }
    }

}

package org.riseger.main.system.compile.function.loop;

import org.apache.log4j.Logger;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Back_fc extends Function_c {
    private static final Logger LOG = Logger.getLogger(Back_fc.class);

    public Back_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        int index = (int) searchMemory.poll();
        LOG.debug("满足条件，跳转至:" + index);

        commandList.jumpTo(index);
    }
}

package org.riseger.main.system.compile.function.update;

import org.apache.log4j.Logger;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.main.system.compile.function.Function_c;
import org.riseger.protocol.exception.SQLException;

public class Update_fc extends Function_c {
    private static final Logger LOG = Logger.getLogger(Update_fc.class);


    public Update_fc() {
        super();
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {

    }

}
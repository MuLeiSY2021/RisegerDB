package org.riseger.main.system.compile.function;

import org.apache.log4j.Logger;
import org.riseger.main.system.compile.compoent.CommandList;
import org.riseger.main.system.compile.compoent.SearchMemory;
import org.riseger.protoctl.exception.SQLException;

public class Entity_fc extends Function_c {
    private static final Logger LOG = Logger.getLogger(Entity_fc.class);

    private final Object _entity;

    public Entity_fc(Object object) {
        super();
        this._entity = object;
    }

    @Override
    public void process(SearchMemory searchMemory, CommandList commandList) throws SQLException {
        searchMemory.push(_entity);
    }

}

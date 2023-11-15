package org.riseger.main.compiler.function;

import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.protoctl.exception.SQLException;

public class Entity_fc extends Function_c {
    private static final Logger LOG = Logger.getLogger(Entity_fc.class);

    private final Object _entity;

    public Entity_fc(Object object, SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
        this._entity = object;
    }

    @Override
    public void process() throws SQLException {
        super.put(_entity);
    }

}

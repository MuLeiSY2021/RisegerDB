package org.riseger.main.compiler.function;

import org.apache.log4j.Logger;
import org.riseger.main.compiler.compoent.CommandList;
import org.riseger.main.compiler.compoent.SearchMemory;
import org.riseger.main.compiler.function.type.UniversalFunction_c;
import org.riseger.protoctl.exception.search.function.IllegalSearchAttributeException;

public class Entity_fc extends UniversalFunction_c {
    private static final Logger LOG = Logger.getLogger(Entity_fc.class);

    private final Object _entity;

    public Entity_fc(Object object, SearchMemory memory, CommandList commandList) {
        super(memory, commandList);
        this._entity = object;
    }

    @Override
    public void process() throws IllegalSearchAttributeException {
        super.put(_entity);
    }

}

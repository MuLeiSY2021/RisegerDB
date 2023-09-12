package org.resegerdb.jrdbc.command.search.function.basic;

import org.resegerdb.jrdbc.command.search.function.type.FUNCTION;

public class ATTRIBUTE implements FUNCTION {


    public String attribute;

    public FUNCTION invoke(String attribute) {
        this.attribute = attribute;
        return this;
    }

}

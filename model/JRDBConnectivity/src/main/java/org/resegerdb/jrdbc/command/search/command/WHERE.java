package org.resegerdb.jrdbc.command.search.command;

import org.resegerdb.jrdbc.command.search.function.type.BOOL_FUNCTION;
import org.resegerdb.jrdbc.driver.session.SearchSession;

public class WHERE extends SQL {
    private BOOL_FUNCTION condition;

    public WHERE(SearchSession session) {
        super(session);
    }

    public WHERE condition(BOOL_FUNCTION function) {
        this.condition = function;
        return this;
    }

    public void warp() {

    }
}

package org.resegerdb.jrdbc.command.search.command;

import org.resegerdb.jrdbc.driver.session.SearchSession;

public abstract class SQL {
    SearchSession session;

    public SQL(SearchSession session) {
        this.session = session;
    }

    protected void warp() {
        session.addSQL(this);
    }
}

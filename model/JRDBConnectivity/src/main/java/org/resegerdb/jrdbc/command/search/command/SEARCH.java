package org.resegerdb.jrdbc.command.search.command;

import org.resegerdb.jrdbc.driver.session.SearchSession;

import java.util.LinkedList;
import java.util.List;

public class SEARCH extends SQL{
    private List<String> content = new LinkedList<>();

    public SEARCH(SearchSession session) {
        super(session);
    }

    public SEARCH attr(String content) {
        this.content.add(content);
        return this;
    }

    public WHERE where() {
        warp();
        return new WHERE(super.session);
    }
}

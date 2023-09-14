package org.riseger.protoctl.search.command;

import java.util.LinkedList;
import java.util.List;

public class SEARCH extends SQL {
    private List<String> content = new LinkedList<>();

    private WHERE where;

    public SEARCH attr(String content) {
        this.content.add(content);
        return this;
    }

    public WHERE where() {
        this.where = new WHERE();
        return this.where;
    }
}

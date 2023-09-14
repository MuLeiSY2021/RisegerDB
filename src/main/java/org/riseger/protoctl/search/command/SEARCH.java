package org.riseger.protoctl.search.command;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;

@Getter
public class SEARCH extends SQL {
    private final List<String> content = new LinkedList<>();

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

package org.riseger.main.system.compile.compoent;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class SearchSet {
    String name;

    List<List<String>> child = new LinkedList<>();

    public SearchSet(String name, List<String> sets) {
        this.name = name;
        this.child.add(sets);
    }

    public void add(List<String> sets) {
        child.add(sets);
    }
}

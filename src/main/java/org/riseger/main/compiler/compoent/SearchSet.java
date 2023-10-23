package org.riseger.main.compiler.compoent;

import lombok.Data;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Data
public class SearchSet {
    String name;

    List<String[]> child = new LinkedList<>();

    public SearchSet(String name, String[] sets) {
        this.name = name;
        this.child.add(sets);
    }

    public void add(String[] sets) {
        child.add(Arrays.copyOfRange(sets, 1, sets.length));
    }
}

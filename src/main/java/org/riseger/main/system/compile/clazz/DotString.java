package org.riseger.main.system.compile.clazz;

import lombok.Getter;

import java.util.LinkedList;

@Getter
public class DotString {
    private final String bottom;

    private final LinkedList<String> parents = new LinkedList<>();

    public DotString(String bottom) {
        this.bottom = bottom;
    }

    public void add(String parent) {
        parents.addFirst(parent);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String parent : parents) {
            sb.append(parent).append(".");
        }
        sb.append(bottom);
        return sb.toString();
    }

    public String getParent() {
        return parents.get(0);
    }

    public String getParent(int index) {
        return parents.get(index);
    }

    public int length() {
        return parents.size();
    }

}

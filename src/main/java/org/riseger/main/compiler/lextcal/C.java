package org.riseger.main.compiler.lextcal;

import org.riseger.utils.tree.Equable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class C implements Equable {
    private final Character character;

    public C(Character character) {
        this.character = character;
    }

    public static Collection<Equable> toCollection(String string) {
        char[] chars = string.toCharArray();
        List<Equable> result = new ArrayList<>(chars.length);
        for (Character c : chars) {
            result.add(new C(c));
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof C) {
            C comp = (C) obj;
            return this.character.equals(comp.character);
        } else {
            return false;
        }
    }
}

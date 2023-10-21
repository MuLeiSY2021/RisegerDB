package org.riseger.main.compiler.lextcal;

import org.riseger.utils.tree.Equable;

public class C implements Equable {
    private final Character character;

    public C(Character character) {
        this.character = character;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Character) {
            return this.character.equals(obj);
        } else {
            return false;
        }
    }
}

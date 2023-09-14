package org.riseger.protoctl.search.function.condition.math;

import org.riseger.protoctl.search.function.entity.NUMBER;

public class MATH_F {
    public static BIG BIG() {
        return new BIG();
    }

    public static BIG_EQUAL BIG_EQUAL() {
        return new BIG_EQUAL();
    }

    public static EQUAL EQUAL() {
        return new EQUAL();
    }

    public static SMALL SMALL() {
        return new SMALL();
    }

    public static SMALL_EQUAL SMALL_EQUAL() {
        return new SMALL_EQUAL();
    }

    public static NUMBER NUMBER() {
        return new NUMBER();
    }


}

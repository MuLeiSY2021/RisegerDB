package org.riseger.protoctl.search.function.math;

import org.riseger.protoctl.search.function.entity.NUMBER;

public class Math_F {
    public static Big_F BIG() {
        return new Big_F();
    }

    public static BigEqual_F BIG_EQUAL() {
        return new BigEqual_F();
    }

    public static Equal_F EQUAL() {
        return new Equal_F();
    }

    public static Small_F SMALL() {
        return new Small_F();
    }

    public static SmallEqual_F SMALL_EQUAL() {
        return new SmallEqual_F();
    }

    public static NUMBER NUMBER() {
        return new NUMBER();
    }


}

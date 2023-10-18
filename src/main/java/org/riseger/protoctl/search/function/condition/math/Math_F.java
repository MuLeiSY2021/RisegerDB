package org.riseger.protoctl.search.function.condition.math;

import org.riseger.protoctl.search.function.entity.NUMBER;

public class Math_F {
    public static Big BIG() {
        return new Big();
    }

    public static BigEqual BIG_EQUAL() {
        return new BigEqual();
    }

    public static Equal EQUAL() {
        return new Equal();
    }

    public static Small SMALL() {
        return new Small();
    }

    public static SmallEqual SMALL_EQUAL() {
        return new SmallEqual();
    }

    public static NUMBER NUMBER() {
        return new NUMBER();
    }


}

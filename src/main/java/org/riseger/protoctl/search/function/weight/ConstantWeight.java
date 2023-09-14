package org.riseger.protoctl.search.function.weight;

public class ConstantWeight {
    public static final int BASIC_AMOUNT = 10000;

    public static final int NOT_KEY_WEIGHT = 100000;


    //Entity functions

    public static final int ATTRIBUTE_WEIGHT = 0;

    public static final int NUMBER_WEIGHT = 3;

    public static final int COORD_WEIGHT = 10;

    public static final int RECTANGLE_WEIGHT = COORD_WEIGHT * NUMBER_WEIGHT;

    public static final int DISTANCE_WEIGHT = COORD_WEIGHT * COORD_WEIGHT;

    //Condition functions
    /*
     *数学函数和非主键图论函数
     */


    public static final int BIG_WEIGHT = 500 + NOT_KEY_WEIGHT;

    public static final int SMALL_WEIGHT = BIG_WEIGHT;

    public static final int BIG_EQUAL_WEIGHT = 505 + NOT_KEY_WEIGHT;

    public static final int SMALL_EQUAL_WEIGHT = BIG_EQUAL_WEIGHT;

    public static final int EQUAL_WEIGHT = 0;
    //Key functions
    /*
     *主键函数
     */
    public static final int IN_WEIGHT = 100;
    public static final int OUT_WEIGHT = 700;

    //Logic functions
    /*
     * 逻辑函数权重无定值，由函数确定
     * */
    public static int AND_WEIGHT(int w1, int w2) {
        return w1 + w2;
    }

    public static int OR_WEIGHT(int w1, int w2) {
        return Math.min(w1, w2);
    }

    public static int NOT_WEIGHT(int w) {
        return BASIC_AMOUNT - w;
    }
}

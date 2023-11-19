package org.risegerdd.client.shell.style;

import java.util.HashMap;
import java.util.Map;

public enum TableColorStyle {
    VERY_SOFT_MAGENTA, SOFT_YELLOW, VERY_SOFT_BLUE, SOFT_RED, DARK_BLUE;

    private static final Map<TableColorStyle, Color> colorMap = new HashMap<>();

    static {
        colorMap.put(VERY_SOFT_MAGENTA, new Color("213"));
        colorMap.put(SOFT_YELLOW, new Color("228"));
        colorMap.put(DARK_BLUE, new Color("62"));
        colorMap.put(VERY_SOFT_BLUE, new Color("87"));
        colorMap.put(SOFT_RED, new Color("197"));
    }

    public static Color color(TableColorStyle color) {
        return colorMap.get(color);
    }

}
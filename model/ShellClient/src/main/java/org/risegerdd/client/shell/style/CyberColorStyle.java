package org.risegerdd.client.shell.style;

import java.util.HashMap;
import java.util.Map;


public enum CyberColorStyle {
    VERY_SOFT_BLUE, VERY_SOFT_MAGENTA, MODERATE_VIOLET, SOFT_RED, DARK_BLUE;

    private static final Map<CyberColorStyle, Color> colorMap = new HashMap<>();

    static {
        colorMap.put(VERY_SOFT_BLUE, new Color("117"));
        colorMap.put(VERY_SOFT_MAGENTA, new Color("213"));
        colorMap.put(MODERATE_VIOLET, new Color("135"));
        colorMap.put(SOFT_RED, new Color("197"));
        colorMap.put(DARK_BLUE, new Color("25"));
    }

    public static Color color(CyberColorStyle color) {
        return colorMap.get(color);
    }

}

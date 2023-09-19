package org.risegerdd.client.shell.style;

public enum CyberColorStyle implements ColorStyle {

    VERY_SOFT_BLUE("117"),
    VERY_SOFT_MAGENTA("213"),

    MODERATE_VIOLET("135"),


    DARK_BLUE("25");
    //        RED("203");
    private final String rgbColor;

    CyberColorStyle(String rgbColor) {
        this.rgbColor = rgbColor;
    }


    @Override
    public String toColor(String s, boolean back) {
        return this.toString(back) + s + ColorStyle.END;
    }

    @Override
    public String toColor(String s) {
        return toColor(s, false);
    }


    public String toString(boolean back) {
        if (back) return ColorStyle.PROMPT_BACK + this.rgbColor + "m";
        return ColorStyle.PROMPT_FRONT + this.rgbColor + "m";
    }

    @Override
    public String toString() {
        return toString(false);
    }
}

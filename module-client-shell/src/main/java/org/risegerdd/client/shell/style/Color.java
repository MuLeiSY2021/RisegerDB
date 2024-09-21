package org.risegerdd.client.shell.style;

public class Color {
    public static String PROMPT_FRONT = "\u001B[38;5;";
    public static String PROMPT_BACK = "\u001B[48;5;";
    public static String END = "\u001B[0m";
    protected final String rgbColor;

    protected Color(String rgbColor) {
        this.rgbColor = rgbColor;
    }


    public String toColor(String s, boolean back) {
        return this.toString(back) + s + END;
    }

    public String toColor(String s) {
        return toColor(s, false);
    }

    public String toColor() {
        return PROMPT_FRONT + this.rgbColor + "m";
    }

    public String toString(boolean back) {
        if (back) return PROMPT_BACK + this.rgbColor + "m";
        return PROMPT_FRONT + this.rgbColor + "m";
    }
}

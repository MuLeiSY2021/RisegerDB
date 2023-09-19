package org.risegerdd.client.shell.style;


import java.util.Random;

public enum ColorList {
    CYBER_COLOR(CyberColorStyle.class);

    private static final Random RAND = new Random();
    final ColorStyle[] colorStyles;
    private Class<? extends ColorStyle> kind;

    ColorList(Class<? extends ColorStyle> kind) {
        this.kind = kind;
        this.colorStyles = kind.getEnumConstants();
    }

    public ColorStyle[] getColorStyles() {
        return colorStyles;
    }

    public String[] toColorfulArray(String text) {
        return toColorfulArray(text, false);
    }

    public String[] toColorfulArray(String text, boolean back) {
        String[] textArray = text.split("\n");
        String[] colorArray = new String[textArray.length];
        int step = textArray.length / this.getColorStyles().length + (textArray.length > this.getColorStyles().length ? 1 : 0);
        int index = 0;
        for (int i = 0; i < textArray.length; i++) {
            colorArray[i] = this.getColorStyles()[index].toColor(textArray[i], back);
            if (i % step == 0) {
                index++;
            }
        }
        return colorArray;
    }

    public String toColorful(String text) {
        return toColorful(text, false);
    }

    public String toColor(String text) {
        return this.getColorStyles()[RAND.nextInt(this.getColorStyles().length)].toString() + text + ColorStyle.END;
    }


    public String toColorful(String text, boolean back) {
        StringBuilder sb = new StringBuilder();
        String[] split = text.split(" ");
        for (int i = 0; i < split.length; i++) {
            sb.append(this.getColorStyles()[i % this.getColorStyles().length].toColor(split[i], back)).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}

package org.risegerdd.client.shell.style;


import lombok.Getter;

import java.util.Random;

@Getter
public enum ColorList {
    CYBER_COLOR();

    private static final Random RAND = new Random();
    final CyberColorStyle[] colorStyles;

    ColorList() {
        this.colorStyles = (CyberColorStyle.class).getEnumConstants();
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
            colorArray[i] = CyberColorStyle.color(this.getColorStyles()[index]).toColor(textArray[i], back);
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
        return CyberColorStyle.color(this.getColorStyles()[RAND.nextInt(this.getColorStyles().length)]).toColor(text);
    }


    public String toColorful(String text, boolean back) {
        StringBuilder sb = new StringBuilder();
        String[] split = text.split(" ");
        for (int i = 0; i < split.length; i++) {
            sb.append(CyberColorStyle.color(this.getColorStyles()[i % this.getColorStyles().length]).toColor(split[i], back)).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}

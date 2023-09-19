package org.risegerdd.client.shell.style;

public interface ColorStyle {
    String PROMPT_FRONT = "\u001B[38;5;";

    String PROMPT_BACK = "\u001B[48;5;";

    String END = "\u001B[0m";

    String toColor(String s, boolean back);

    String toColor(String s);
}
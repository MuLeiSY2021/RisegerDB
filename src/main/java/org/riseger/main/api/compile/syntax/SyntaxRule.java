package org.riseger.main.api.compile.syntax;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SyntaxRule {
    private final Map<String, Rule> ruleMap;

    public SyntaxRule(Map<String, Rule> ruleMap) {
        this.ruleMap = ruleMap;
    }

    public static SyntaxRule newSyntaxRule(String ruleText) {
        Map<String, Rule> eachRule = new HashMap<String, Rule>();
        Rule prevRule = null;
        for (String ruleTextLine : ruleText.split("\n")) {
            String[] tokens;
            if (!ruleTextLine.startsWith("   |")) {
                tokens = ruleTextLine.split("->");
                prevRule = new Rule(tokens[0]);
                eachRule.put(tokens[0], prevRule);
                tokens = tokens[1].split("\\|");
            } else {
                tokens = ruleTextLine.split("\\|");
            }
            for (String meta : tokens) {
                prevRule.newMeta();
                for (String tile : meta.split(" ")) {
                    prevRule.add(tile, tile.startsWith("\""));
                }
            }
        }
        return new SyntaxRule(eachRule);
    }

    static class Rule {
        private final String type;

        private final List<Meta> meta = new LinkedList<Meta>();

        private Meta tmp;

        public Rule(String type) {
            this.type = type;
        }

        public void newMeta() {
            tmp = new Meta();
            this.meta.add(tmp);
        }

        public void add(String type, boolean key) {
            tmp.add(type, key);
        }
    }

    private static class Meta {
        private List<Type> tiles = new LinkedList<>();

        public void add(String type, boolean key) {
            this.tiles.add(new Type(key, type));
        }
    }

    private static class Type {
        boolean key;

        private String value;

        public Type(boolean key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}

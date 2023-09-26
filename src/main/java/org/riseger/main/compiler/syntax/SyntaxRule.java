package org.riseger.main.compiler.syntax;

import lombok.Data;
import lombok.Getter;

import java.util.*;

@Getter
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
                if (tokens[0].startsWith("END_")) {
                    prevRule = new Rule(tokens[0].substring("END_".length()), true);
                } else {
                    prevRule = new Rule(tokens[0], false);
                }
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

    @Data
    static class Rule {
        private final String type;

        private final boolean end;

        private final List<Meta> meta = new LinkedList<Meta>();

        private Meta tmp;


        public Rule(String type, boolean end) {
            this.type = type;
            this.end = end;
        }

        public void newMeta() {
            tmp = new Meta();
            this.meta.add(tmp);
        }

        public void add(String type, boolean key) {
            tmp.add(type, key);
        }
    }

    @Data
    static class Meta {
        private List<Type> tiles = new LinkedList<>();

        public void add(String type, boolean key) {
            this.tiles.add(new Type(key, type));
        }
    }

    @Data
    static class Type {
        boolean key;

        private String value;

        public Type(boolean key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Type type = (Type) o;
            return Objects.equals(value, type.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}

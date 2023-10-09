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
        Map<String, Rule> eachRule = new HashMap<>();
        Rule prevRule = null;
        for (String ruleTextLine : ruleText.split("\n")) {
            if (ruleTextLine.isEmpty()) {
                continue;
            }
            String[] tokens;
            if (!ruleTextLine.startsWith("    |")) {
                tokens = ruleTextLine.split("->");
                if (tokens[0].startsWith("END:")) {
                    String name = tokens[0].substring("END:".length());
                    prevRule = new Rule(name, true);
                    eachRule.put(name, prevRule);

                } else {
                    prevRule = new Rule(tokens[0], false);
                    eachRule.put(tokens[0].replace(" ", ""), prevRule);

                }
            } else {
                tokens = ruleTextLine.split("\\|");
            }
            if (tokens.length > 1 && prevRule != null) {
                prevRule.newMeta();
                for (String meta : tokens[1].split(" ")) {
                    if (meta.isEmpty()) {
                        continue;
                    }
                    prevRule.add(meta, meta.startsWith("\""));
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
            if (key) {
                this.value = value.substring(1, value.length() - 1);
            } else {
                this.value = value;
            }
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

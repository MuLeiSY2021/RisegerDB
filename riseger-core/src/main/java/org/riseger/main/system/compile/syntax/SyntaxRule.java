package org.riseger.main.system.compile.syntax;

import lombok.Data;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.riseger.protocol.compiler.function.Function_f;
import org.riseger.protocol.compiler.function.Null_f;
import org.riseger.utils.tree.Equable;

import java.util.*;

@Getter
public class SyntaxRule {
    public static final Logger LOG = Logger.getLogger(SyntaxRule.class);
    private final Map<String, Rule> ruleMap;

    private Class<Function_f> endFunction;

    public SyntaxRule(String ruleText) throws ClassNotFoundException {
        Map<String, Rule> eachRule = new HashMap<>();
        this.ruleMap = eachRule;
        Rule prevRule = null;
        for (String ruleTextLine : ruleText.split("\n")) {
            if (ruleTextLine.isEmpty() | ruleTextLine.startsWith("//")) {
                continue;
            }
            String[] tokens;
            if (!ruleTextLine.startsWith("    |")) {
                tokens = ruleTextLine.split("->");
                if (tokens[0].startsWith("END:")) {
                    String[] endTokens = tokens[0].substring("END:".length()).split(":>");
                    String name = endTokens[0].replace(" ", "");
                    prevRule = new Rule(name, true);
                    eachRule.put(name, prevRule);
                    this.endFunction = (Class<Function_f>) Class.forName(endTokens[1].replace(" ", ""));
                } else {
                    prevRule = new Rule(tokens[0], false);
                    eachRule.put(tokens[0].replace(" ", ""), prevRule);
                }
            } else {
                tokens = ruleTextLine.split("\\|");
            }
            if (tokens.length > 1 && prevRule != null) {
                prevRule.newMeta();
                String[] tmp;
                Class<Function_f> functionClazz = null;
                if ((tmp = tokens[1].split(":>")).length > 1) {
                    try {
                        Class<?> tmp_c = Class.forName(tmp[1].replace(" ", ""));
                        if (Function_f.class.isAssignableFrom(tmp_c)) {
                            functionClazz = (Class<Function_f>) tmp_c;
                        } else {
                            throw new ClassCastException();
                        }
                    } catch (ClassNotFoundException | ClassCastException e) {
                        LOG.error("Function" + tokens[1] + " is not assignable from Function_f", e);
                        throw e;
                    }
                } else {
                    Class<?> clazz = Null_f.class;
                    functionClazz = (Class<Function_f>) clazz;
                }
                tokens[1] = tmp[0];
                for (String meta : tokens[1].split(" ")) {
                    if (meta.isEmpty()) {
                        continue;
                    }
                    prevRule.add(meta, meta.startsWith("\""), functionClazz);
                }
            }
        }
    }

    @Data
    public static class Meta {
        private List<Tile> tiles = new LinkedList<>();

        private Class<Function_f> functionClazz;

        public void add(String type, boolean key, Class<Function_f> functionClazz) {
            this.tiles.add(new Tile(key, type));
            this.functionClazz = functionClazz;
        }
    }

    @Data
    public static class Tile implements Equable {
        boolean key;

        private String value;

        public Tile(boolean key, String value) {
            this.key = key;
            if (key) {
                this.value = value.substring(1, value.length() - 1);
            } else {
                this.value = value;
            }
        }

        @Override
        public boolean equal(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return Objects.equals(value, tile.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    @Data
    public class Rule {
        private final String type;

        private final boolean end;

        private final List<Meta> meta = new LinkedList<>();

        private Meta tmp;

        private int typeId;


        public Rule(String type, boolean end) {
            this.type = type;
            this.end = end;
            this.typeId = SyntaxRule.this.ruleMap.size();
        }

        public void newMeta() {
            tmp = new Meta();
            this.meta.add(tmp);
        }

        public void add(String type, boolean key, Class<Function_f> functionClazz) {
            tmp.add(type, key, functionClazz);
        }

    }
}

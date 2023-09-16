package org.riseger.main.api.compile.syntax;

import lombok.Data;
import org.apache.log4j.Logger;
import org.riseger.main.api.compile.Compiler;
import org.riseger.main.api.compile.config.Config;
import org.riseger.main.api.compile.context.Context;
import org.riseger.main.api.compile.tokenize.Token;
import org.riseger.main.api.compile.tokenize.Tokenizer;
import org.riseger.main.api.compile.tree.KeywordsTree;
import org.riseger.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Data
public class Function {
    public static final List<Function> FUNCTIONS = new LinkedList<>();
    public static int maxPriority = 1;
    public static Map<String, Object> typeMap = new HashMap<>();
    private static int count = 0;
    public static final Logger LOG = Logger.getLogger(Function.class);

    static {
        try {
            String file = Utils.getText(new File("data/basic.txt"));
            if (file != null) {
                for (String line : file.split("\n")) {
                    FUNCTIONS.add(new Function(line));
                }
            }
            file = Utils.getText(new File("data/function.txt"));
            if (file != null) {
                for (String line : file.split("\n")) {
                    FUNCTIONS.add(new Function(line, true));
                }
            }
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        }
    }

    static {
        typeMap.put("int", Integer.class);
        typeMap.put("float", Float.class);
        typeMap.put("double", Double.class);
        typeMap.put("char", Character.class);
        typeMap.put("boolean", Boolean.class);
        typeMap.put("void", Void.class);
    }

    private final List<String> list = new LinkedList<>();

    private final List<String> sourceCodeList = new LinkedList<>();

    private int priority;

    private String returnType;

    public Function(String line) {
        String[] tmp = line.split("#:");
        Context context = new Context(tmp[1]);
        Compiler compiler = new Compiler(context);
        compiler.tokenAnalyze();
        for (Token token : context.getTokens()) {
            if (!token.getCode().startsWith(Config.KEYWORD_PREFIX)) {
                list.add(Config.TYPE_PREFIX + Config.SPLIT_PREFIX + token.getCode().split(Config.SPLIT_PREFIX)[0]);
            } else {
                list.add(token.getCode());
            }
        }

        for (Token token : context.getTokens()) {
            sourceCodeList.add(token.getSourceCode().toUpperCase());
        }


        priority = Integer.parseInt(tmp[2]);
        if (priority > maxPriority) {
            maxPriority = priority;
        }
    }

    public Function(String line, boolean isFunction) {
        String[] tmp = line.split(" ");
        sourceCodeList.addAll(Tokenizer.splitToken(tmp[1]));

        for (String word : sourceCodeList) {
            String code = KeywordsTree.INSTANCE.getCode(word.toUpperCase());
            if (code != null) {
                list.add(code);
            } else {
                list.add(Config.TYPE_PREFIX + Config.SPLIT_PREFIX + word.toUpperCase());
            }
        }
        returnType = Config.TYPE_PREFIX + Config.SPLIT_PREFIX + tmp[0].replace(" ", "").toUpperCase();
        list.addAll(Tokenizer.splitToken(tmp[1]));
        priority = 1;
    }
}

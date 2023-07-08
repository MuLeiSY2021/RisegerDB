package org.risegerdb.compile.lextcal;

import lombok.Data;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.tokenize.Tokenizer;
import org.risegerdb.utils.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class Function {

    public static final Map<String,Function> FUNCTIONS_BY_KEY = Function.getFunctionsByKey(CompileConfig.FUNCTION_CONFIG_URI);

    public static final Map<String,Function> FUNCTIONS_BY_ID = Function.getFunctionsById();


    private static int COUNT = 0;

    private String key;

    private int tokenId;

    private String returnType;

    private String syntax;

    private int priority;

    public Function(Element function) {
        try {
            this.key = function.elementText("key");
            this.tokenId = COUNT++;
            this.priority = Integer.parseInt(function.elementText("priority"));
            this.returnType = function.elementText("return");

            this.syntax = function.elementText("syntax");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(function.elementText("key"));
        }
    }

    public Function(String key) {
        this.key = key;
        this.tokenId = COUNT++;
        this.priority = CompileConfig.DEFAULT_PRIORITY;
        this.returnType = null;
        this.syntax = null;
    }

    public static Map<String,Function> getFunctionsByKey(String uri) {
        Map<String,Function> res = new HashMap<>();
        try {
            File file = new File(uri);
            SAXReader reader = new SAXReader();
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            List<Element> functions = root.elements("fun");
            for (Element fun:functions) {
                res.put(fun.elementText("key"),new Function(fun));
            }
            Map<String,String> keywords = new HashMap<>();
            String[] otherKeywords = Utils.getText(new File(CompileConfig.OTHER_KEYWORDS_FILE_URI)).split(" ");

            for (String key:otherKeywords) {
                if(key.endsWith("\n")) {
                    key = key.substring(0, key.length() - 1);
                }
                keywords.put(key,key);
            }
            for (String key:keywords.values()) {
                res.put(key,new Function(key));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Map<String,Function> getFunctionsById() {
        Map<String,Function> res = new HashMap<>();

        for (Function function:Function.FUNCTIONS_BY_KEY.values()) {
            res.put(function.getId(), function);
        }
        return res;
    }

    public String getId() {
        return CompileConfig.KEYWORD_CONST_PREFIX + "_" + tokenId;
    }

    public List<String> getSyntaxList() {
        List<String> syntaxList = Tokenizer.INSTANCE.execute(syntax);
        syntaxList.replaceAll((word) -> {
            if(word.equals("KEY")) {
                return CompileConfig.KEYWORD_CONST_PREFIX + "_" + tokenId;
            }
            return word;
        });
        return syntaxList;
    }
}

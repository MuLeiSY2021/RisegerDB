package org.risegerdb.compile.syntax;

import lombok.Data;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.tokenize.Tokenizer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
public class Function {

    public static final Map<String,Function> FUNCTIONS = Function.getFunction(CompileConfig.FUNCTION_CONFIG_URI);

    private static int COUNT = 0;

    private String key;

    private int tokenId;

    private String returnType;

    private List<String> syntax;

    private int priority;

    public Function(Element function) {
        try {
            this.key = function.elementText("key");
            this.tokenId = COUNT++;
            this.priority = Integer.parseInt(function.elementText("priority"));
            this.returnType = function.elementText("return");
            String syntax = function.elementText("syntax");
            syntax = syntax.replaceAll("KEY", this.key);
            this.syntax = Tokenizer.INSTANCE.execute(syntax);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(function.elementText("key"));
        }
    }

    public static Map<String,Function> getFunction(String uri) {
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
        }catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}

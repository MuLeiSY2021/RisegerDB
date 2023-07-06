package org.risegerdb.utils;

import com.google.gson.Gson;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.risegerdb.compile.config.CompileConfig;
import org.risegerdb.compile.tokenize.Token;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReGenerateFile {
    public static void main(String[] args) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File("./src/main/resources/function_config.xml"));
        List<Token> keywords = new ArrayList<>();
        for (Element fun:(List<Element>)document.getRootElement().elements("fun")) {
            String key= fun.elementText("key").toLowerCase();
            if(!keywords.contains(Token.tmpToken(key))) {
                keywords.add(new Token(key));
            }
            key = fun.elementText("key").toUpperCase();
            if(!keywords.contains(Token.tmpToken(key))) {
                keywords.add(new Token(key));
            }
        }
        String[] otherKeywords = Utils.getText(new File(CompileConfig.OTHER_KEYWORDS_FILE_URI)).split(" ");
        for (String key:otherKeywords) {
            if(key.endsWith("\n")) {
                key = key.substring(0, key.length() - 1);
            }
            if(!keywords.contains(Token.tmpToken(key))) {
                keywords.add(new Token(key));
            }
        }
        File keywordsFile = new File("./src/main/resources/keywords.json");
        try (FileOutputStream out = new FileOutputStream(keywordsFile)){
            Gson gson = new Gson();
            out.write(gson.toJson(keywords).getBytes(StandardCharsets.UTF_8));
        }
    }
}

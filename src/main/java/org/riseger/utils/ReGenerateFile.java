package org.riseger.utils;

public class ReGenerateFile {
//    public static void main(String[] args) throws Exception {
//        SAXReader reader = new SAXReader();
//        Document document = reader.read(new File("./src/main/resources/function_config.xml"));
//        List<Token> keywords = new ArrayList<>();
//        for (Element fun:(List<Element>)document.getRootElement().elements("fun")) {
//            String key= fun.elementText("key").toLowerCase();
//            if(!keywords.contains(Token.tmpToken(key))) {
//                keywords.add(new Token(key));
//            }
//            key = fun.elementText("key").toUpperCase();
//            if(!keywords.contains(Token.tmpToken(key))) {
//                keywords.add(new Token(key));
//            }
//        }
//        String[] otherKeywords = Utils.getText(new File(CompileConfig.OTHER_KEYWORDS_FILE_URI)).split(" ");
//        for (String key:otherKeywords) {
//            if(key.endsWith("\n")) {
//                key = key.substring(0, key.length() - 1);
//            }
//            if(!keywords.contains(Token.tmpToken(key))) {
//                keywords.add(new Token(key));
//            }
//        }
//        File keywordsFile = new File("./src/main/resources/keywords.json");
//        try (FileOutputStream Out_F = new FileOutputStream(keywordsFile)){
//            Gson gson = new Gson();
//            Out_F.write(gson.toJson(keywords).getBytes(StandardCharsets.UTF_8));
//        }
//    }
}

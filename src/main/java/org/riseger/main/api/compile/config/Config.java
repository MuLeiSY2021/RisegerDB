package org.riseger.main.api.compile.config;

import org.riseger.main.api.compile.tree.KeywordsTree;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final String FUNCTION_CONFIG_URI = "./src/main/resources/function_config.xml";

    public static final String KEYWORDS_FILE_URI = "./src/main/resources/keywords.json";

    public static final String OTHER_KEYWORDS_FILE_URI = "./src/main/resources/otherKeywords.config";


    public static final String STRING_PREFIX = "STR";

    public static final String NUMBER_PREFIX = "NUM";

    public static final String KEYWORD_PREFIX = "KW";

    public static final String SPLIT_PREFIX = "_";

    public static final String TYPE_PREFIX = "TYP";

    public static final String NUMBER_PATTERN = "([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    public static final String WORD_PATTERN =
            "((?!\\d+(\\.\\d+)?)\\w+|^('')?$|'[^']*')";

    public static final String TOKEN_PATTERN =
            "((?!\\d+(\\.\\d+)?)\\w+|^('')?$|'[^']*')|([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";

    private static final String[] LINEFEED_KEYWORDS = new String[]{
            "SELECT",
            "WHERE",
            "USE",
            "FROM",
            "ON",
            "OTHER"
    };

    public static final Map<String, Boolean> LINEFEED_MAP = new HashMap<>();

    static {
        for (String kerword : LINEFEED_KEYWORDS) {
            LINEFEED_MAP.put(KeywordsTree.INSTANCE.getCode(kerword), true);
        }
    }

    public static final int DEFAULT_PRIORITY = Integer.MAX_VALUE;

    public static final int MORE_PRIORITY = 1024;
}

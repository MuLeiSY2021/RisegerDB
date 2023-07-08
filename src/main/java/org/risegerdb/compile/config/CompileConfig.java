package org.risegerdb.compile.config;

import org.risegerdb.compile.lextcal.LexicalTree;

import java.util.HashMap;
import java.util.Map;

public class CompileConfig {
    public static final String FUNCTION_CONFIG_URI = "./src/main/resources/function_config.xml";

    public static final String KEYWORDS_FILE_URI = "./src/main/resources/keywords.json";

    public static final String OTHER_KEYWORDS_FILE_URI = "./src/main/resources/otherKeywords.config";


    public static final String STRING_CONST_PREFIX = "STR";

    public static final String NUMBER_CONST_PREFIX = "NUM";

    public static final String KEYWORD_CONST_PREFIX = "KW";

    public static final String VAR_CONST_PREFIX = "VAR";

    public static final String BOOL_CONST_PREFIX = "BOL";

    public static final String RECT_CONST_PREFIX = "RECT";

    public static final String CYCLE_CONST_PREFIX = "CYCLE";



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

    public static final Map<String,Boolean> LINEFEED_MAP = new HashMap<>();
    static {
        for (String kerword : LINEFEED_KEYWORDS) {
            LINEFEED_MAP.put(LexicalTree.INSTANCE.getId(kerword), true);
        }
    }
    
    public static final int DEFAULT_PRIORITY = 1024;
}

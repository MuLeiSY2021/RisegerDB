package org.risegerdb.compile.config;

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
            "('[a-zA-Z_.]+'|[a-zA-Z_.]+|[_])";

    public static final String TOKEN_PATTERN =
            "('[a-zA-Z_.]+'|[a-zA-Z_.]+|[_])|([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])";
}

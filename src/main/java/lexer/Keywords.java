package lexer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Keywords {
    static String[] keywords = {
            "var",
            "if",
            "else",
            "for",
            "while",
            "do",
            "break",
            "func",
            "return",
            // 类型
            "int",
            "float",
            "string",
            "boolean",
            "void",
    };

    static Set<String> set = new HashSet<>(Arrays.asList(keywords));

    public static boolean isKeyword(String word) {
        return set.contains(word);
    }
}

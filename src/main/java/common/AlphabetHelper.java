package common;

import java.util.regex.Pattern;

public class AlphabetHelper {
    static Pattern letterPattern = Pattern.compile("^[a-zA-Z]$");
    static Pattern numberPattern = Pattern.compile("^[0-9]$");
    static Pattern literalPattern = Pattern.compile("^[_a-zA-Z0-9]$");
    static Pattern operatorPattern = Pattern.compile("^[+\\\\*\\-/<>=!&|^%]$");

    public static boolean isLetter(char c) {
        return letterPattern.matcher(c + "").matches();
    }
    public static boolean isNumber(char c) {
        return numberPattern.matcher(c + "").matches();
    }
    public static boolean isLiteral(char c) {
        return literalPattern.matcher(c + "").matches();
    }
    public static boolean isOperator(char c) {
        return operatorPattern.matcher(c + "").matches();
    }
}

package lexer;

import common.AlphabetHelper;
import common.PeekIterator;
import exceptions.LexicalException;

public class Token {
    TokenType _type;
    String _value;

    /**
     * 提取变量或关键字
     *
     * @param iterator
     * @return
     */
    public static Token makeVarOrKeyword(PeekIterator<Character> iterator) {
        String s = "";

        while (iterator.hasNext()) {
            char lookahead = iterator.peek();
            if (AlphabetHelper.isLiteral(lookahead)) {
                s += lookahead;
            } else {
                break;
            }
            // 确定是变量or关键字才去真正地消费它
            iterator.next();
        }
        // 判断关键词
        if (Keywords.isKeyword(s)) {
            return new Token(TokenType.KEYWORD, s);
        }
        if (s.equals("true") || s.equals("false")) {
            return new Token(TokenType.BOOLEAN, s);
        }
        return new Token(TokenType.VARIABLE, s);
    }

    public static Token makeString(PeekIterator<Character> iterator) throws LexicalException {
        StringBuilder s = new StringBuilder();
        int state = 0;

        while (iterator.hasNext()) {
            char c = iterator.next();
            // 状态机 0 -> 1 or 2 -> 在状态1或状态2循环 -> 到达条件，终止 -> result
            switch (state) {
                case 0:
                    if (c == '"') {
                        state = 1;
                    } else {
                        state = 2;
                    }
                    s.append(c);
                    break;
                case 1:
                    if (c == '"') {
                        return new Token(TokenType.STRING, s.toString() + c);
                    } else {
                        s.append(c);
                    }
                    break;
                case 2:
                    if (c == '\'') {
                        return new Token(TokenType.STRING, s.toString() + c);
                    } else {
                        s.append(c);
                    }
                    break;
            }
        }

        throw new LexicalException("Unexpected  error");
    }

    public static Token makeOperator(PeekIterator<Character> iterator) throws LexicalException {
        StringBuilder s = new StringBuilder();
        int state = 0;

        while (iterator.hasNext()) {
            char lookahead = iterator.next();
            switch (state) {
                case 0:
                    switch (lookahead) {
                        case '+':
                            state = 1;
                            break;
                        case '-':
                            state = 2;
                            break;
                        case '*':
                            state = 3;
                            break;
                        case '/':
                            state = 4;
                            break;
                        case '>':
                            state = 5;
                            break;
                        case '<':
                            state = 6;
                            break;
                        case '=':
                            state = 7;
                            break;
                        case '!':
                            state = 8;
                            break;
                        case '&':
                            state = 9;
                            break;
                        case '|':
                            state = 10;
                            break;
                        case '%':
                            state = 11;
                            break;
                        case '^':
                            return new Token(TokenType.OPERATOR, "^");
                        case ',':
                            return new Token(TokenType.OPERATOR, ",");
                        case ';':
                            return new Token(TokenType.OPERATOR, ";");
                    }
                    break;
                case 1:
                    if (lookahead == '+') {
                        return new Token(TokenType.OPERATOR, "++");
                    }
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "+=");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "+");
                case 2:
                    if (lookahead == '-') {
                        return new Token(TokenType.OPERATOR, "--");
                    }
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "-=");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "-");
                case 3:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "*=");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "*");
                case 4:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "/=");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "/");
                case 5:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, ">=");
                    }
                    if (lookahead == '>') {
                        return new Token(TokenType.OPERATOR, ">>");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, ">");
                case 6:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "<=");
                    }
                    if (lookahead == '<') {
                        return new Token(TokenType.OPERATOR, "<<");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "<");
                case 7:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "==");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "==");
                case 8:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "!=");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "!");
                case 9:
                    if (lookahead == '&') {
                        return new Token(TokenType.OPERATOR, "&&");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "&");
                case 10:
                    if (lookahead == '|') {
                        return new Token(TokenType.OPERATOR, "||");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "|");
                case 11:
                    if (lookahead == '=') {
                        return new Token(TokenType.OPERATOR, "%=");
                    }
                    iterator.putBack();
                    return new Token(TokenType.OPERATOR, "%");
            }
        }
        throw new LexicalException("Unexpected  error");
    }

    public static Token makeNumber(PeekIterator<Character> iterator) throws LexicalException {
        StringBuilder s = new StringBuilder();
        int state = 0;
        while (iterator.hasNext()) {
            char lookahead = iterator.peek();

            switch (state) {
                case 0:
                    if (lookahead == '0') {
                        state = 1;
                    } else if (AlphabetHelper.isNumber(lookahead)) {
                        state = 2;
                    } else if (lookahead == '+' || lookahead == '-') {
                        state = 3;
                    } else if (lookahead == '.') {
                        state = 4;
                    }
                    break;
                // 0 开头
                case 1:
                    if (lookahead == '0') {
                        state = 1;
                    } else if (AlphabetHelper.isNumber(lookahead)) {
                        state = 2;
                    } else if (lookahead == '.') {
                        state = 4;
                    } else {
                        return new Token(TokenType.INTEGER, s.toString());
                    }
                    break;
                // 1-9开头
                case 2:
                    if (AlphabetHelper.isNumber(lookahead)) {
                        state = 2;
                    } else if (lookahead == '.') {
                        state = 4;
                    } else {
                        return new Token(TokenType.INTEGER, s.toString());
                    }
                    break;
                // +-号开头
                case 3:
                    if (AlphabetHelper.isNumber(lookahead)) {
                        state = 2;
                    } else if (lookahead == '.') {
                        state = 5;
                    } else {
                        throw new LexicalException(lookahead);
                    }
                    break;
                // 处理float
                case 4:
                    if (lookahead == '.') {
                        throw new LexicalException(lookahead);
                    } else if (AlphabetHelper.isNumber(lookahead)) {
                        state = 20;
                    } else {
                        return new Token(TokenType.FLOAT, s.toString());
                    }
                    break;
                // 处理+-号开头浮点数
                case 5:
                    if (AlphabetHelper.isNumber(lookahead)) {
                        state = 20;
                    } else {
                        throw new LexicalException(lookahead);
                    }
                    break;
                case 20:
                    if (AlphabetHelper.isNumber(lookahead)) {
                        state = 20;
                    } else if (lookahead == '.') {
                        throw new LexicalException(lookahead);
                    } else {
                        return new Token(TokenType.FLOAT, s.toString());
                    }
            }
            // end switch
            iterator.next();
            s.append(lookahead);
        }
        throw new LexicalException("Unexpected error");
    }

    public Token(TokenType _type, String _value) {
        this._type = _type;
        this._value = _value;
    }

    public TokenType getType() {
        return _type;
    }

    public String getValue() {
        return _value;
    }

    @Override
    public String toString() {
        return String.format("type %s, value %s", _type, _value);
    }

    /**
     * token是否为变量
     *
     * @return bool
     */
    public boolean isVariable() {
        return _type == TokenType.VARIABLE;
    }

    /**
     * token是否为值类型
     *
     * @return bool
     */
    public boolean isScalar() {
        return _type == TokenType.INTEGER || _type == TokenType.FLOAT
                || _type == TokenType.STRING || _type == TokenType.BOOLEAN;
    }

    public boolean isNumber() {
        return _type == TokenType.INTEGER || _type == TokenType.FLOAT;
    }

    public boolean isOperator() {
        return _type == TokenType.OPERATOR;
    }
}

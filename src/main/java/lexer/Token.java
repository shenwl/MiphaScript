package lexer;

import common.AlphabetHelper;
import common.PeekIterator;

public class Token {
    TokenType _type;
    String _value;

    /**
     * 提取变量或关键字
     * @param iterator
     * @return
     */
    public static Token makeVarOrKeyword(PeekIterator<Character> iterator) {
        String s = "";

        while(iterator.hasNext()) {
            char lookahead = iterator.peek();
            if(AlphabetHelper.isLiteral(lookahead)) {
                s += lookahead;
            } else {
                break;
            }
            // 确定是变量or关键字才去真正地消费它
            iterator.next();
        }
        // 判断关键词
        if(Keywords.isKeyword(s)) {
            return new Token(TokenType.KEYWORD, s);
        }
        if(s.equals("true") || s.equals("false")) {
            return new Token(TokenType.BOOLEAN, s);
        }
        return new Token(TokenType.VARIABLE, s);
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
     * @return bool
     */
    public boolean isVariable() {
        return _type == TokenType.VARIABLE;
    }

    /**
     * token是否为值类型
     * @return bool
     */
    public boolean isScalar() {
        return _type == TokenType.INTEGER || _type == TokenType.FLOAT
                || _type == TokenType.STRING || _type == TokenType.BOOLEAN;
    }
}

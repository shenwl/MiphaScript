package parser.utils;

import lexer.Token;

public class ParserException extends Exception {
    private String msg;

    public ParserException(String msg) {
        this.msg = msg;
    }

    public ParserException(Token token) {
        this.msg = String.format("Syntax Error, unexpected token %s", token.getValue());
    }

    @Override
    public String getMessage() {
        return msg;
    }
}

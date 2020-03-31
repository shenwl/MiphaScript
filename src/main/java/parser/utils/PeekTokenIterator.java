package parser.utils;

import common.PeekIterator;
import lexer.Token;
import lexer.TokenType;

import java.util.stream.Stream;

public class PeekTokenIterator extends PeekIterator<Token> {
    public PeekTokenIterator(Stream<Token> stream) {
        super(stream);
    }

    public Token nextMatch(String value) throws ParserException {
        Token token = this.next();
        if (!token.getValue().equals(value)) {
            throw new ParserException(token);
        }
        return token;
    }

    public Token nextMatch(TokenType type) throws ParserException {
        Token token = this.next();
        if (!token.getType().equals(type)) {
            throw new ParserException(token);
        }
        return token;
    }
}

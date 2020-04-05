package parser.ast;

import lexer.Token;
import lexer.TokenType;
import parser.utils.PeekTokenIterator;

public abstract class Factor extends ASTNode {
    public Factor(ASTNode parent, Token token) {
        super(parent);
        this.lexeme = token;
        this.label = token.getValue();
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) {
        Token token = it.peek();
        TokenType type = token.getType();

        if(type == TokenType.VARIABLE) {
            it.next();
            return new Variable(parent, token);
        } else if(token.isScalar()) {
            it.next();
            return new Scalar(parent, token);
        }
        return null;
    }
}

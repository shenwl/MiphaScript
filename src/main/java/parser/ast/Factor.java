package parser.ast;

import lexer.Token;
import lexer.TokenType;
import parser.utils.PeekTokenIterator;

public abstract class Factor extends ASTNode {
    public Factor(Token token) {
        super();
        this.lexeme = token;
        this.label = token.getValue();
    }

    public static ASTNode parse(PeekTokenIterator it) {
        Token token = it.peek();

        if (token.isVariable()) {
            it.next();
            return new Variable(token);
        } else if (token.isScalar()) {
            it.next();
            return new Scalar(token);
        }
        return null;
    }
}

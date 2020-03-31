package parser.ast;

import lexer.Token;
import lexer.TokenType;
import parser.utils.PeekTokenIterator;

public abstract class Factor extends ASTNode {
    public Factor(ASTNode parent, PeekTokenIterator it) {
        super(parent);
        Token token = it.next();
        TokenType tokenType = token.getType();

        if(tokenType == TokenType.VARIABLE) {
            this.type = ASTNodeTypes.VARIABLE;
        } else {
            this.type = ASTNodeTypes.SCALAR;
        }

        this.label = token.getValue();
        this.lexeme = token;
    }
}

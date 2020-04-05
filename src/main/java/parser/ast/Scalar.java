package parser.ast;

import lexer.Token;

public class Scalar extends Factor {
    public Scalar(ASTNode parent, Token token) {
        super(parent, token);
    }
}

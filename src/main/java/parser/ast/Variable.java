package parser.ast;

import lexer.Token;

public class Variable extends Factor {
    public Variable(ASTNode parent, Token token) {
        super(parent, token);
        this.type = ASTNodeTypes.VARIABLE;
    }
}

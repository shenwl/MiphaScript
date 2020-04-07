package parser.ast;

import lexer.Token;

public class Variable extends Factor {
    public Variable(Token token) {
        super(token);
        this.type = ASTNodeTypes.VARIABLE;
    }
}

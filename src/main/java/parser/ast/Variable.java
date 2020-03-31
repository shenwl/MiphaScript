package parser.ast;

import parser.utils.PeekTokenIterator;

public class Variable extends Factor {
    public Variable(ASTNode parent, PeekTokenIterator it) {
        super(parent, it);
    }
}

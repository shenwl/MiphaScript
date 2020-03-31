package parser.ast;

import parser.utils.PeekTokenIterator;

public class Scalar extends Factor {
    public Scalar(ASTNode parent, PeekTokenIterator it) {
        super(parent, it);
    }
}

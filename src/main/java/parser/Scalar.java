package parser;

public class Scalar extends Factor {
    public Scalar(ASTNode parent) {
        super(parent, ASTNodeTypes.SCALAR, null);
    }
}

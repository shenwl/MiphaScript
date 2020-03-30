package parser;

public class Variable extends Factor {
    public Variable(ASTNode parent) {
        super(parent, ASTNodeTypes.VARIABLE, null);
    }
}

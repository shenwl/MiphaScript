package parser;

public abstract class Factor extends ASTNode {
    public Factor(ASTNode parent, ASTNodeTypes type, String label) {
        super(parent, type, label);
    }
}

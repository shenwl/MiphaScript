package parser.ast;

public abstract class Stmt extends ASTNode {
    public Stmt(ASTNode parent, ASTNodeTypes type, String label) {
        super(parent, type, label);
    }
}

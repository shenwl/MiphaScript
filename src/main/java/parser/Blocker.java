package parser;

public class Blocker extends Stmt {
    public Blocker(ASTNode parent) {
        super(parent, ASTNodeTypes.BLOCK, "block");
    }
}
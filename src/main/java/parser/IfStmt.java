package parser;

public class IfStmt extends Stmt {
    public IfStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.IF_STMT, "if");
    }
}

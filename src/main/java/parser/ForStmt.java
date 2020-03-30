package parser;

public class ForStmt extends Stmt {
    public ForStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.FOR_STMT, "for");
    }
}

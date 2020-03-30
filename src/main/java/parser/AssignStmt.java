package parser;

public class AssignStmt extends Stmt {
    public AssignStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.ASSIGN_STMT, "assign");
    }
}

package parser.ast;

public class DeclareStmt extends Stmt {
    public DeclareStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.DECLARE_STMT, "declare");
    }
}

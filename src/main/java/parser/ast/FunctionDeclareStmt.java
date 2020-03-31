package parser.ast;

public class FunctionDeclareStmt extends Stmt {
    public FunctionDeclareStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.FUNCTION_DECLARE_STMT, "function");
    }
}

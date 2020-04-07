package parser.ast;

import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class ReturnStmt extends Stmt {
    public ReturnStmt(ASTNode parent) {
        super(parent, ASTNodeTypes.RETURN_STMT, "return");
    }

    public static ASTNode parse(ASTNode parent, PeekTokenIterator it) throws ParserException {
        it.nextMatch("return");
        ASTNode returnStmt = new ReturnStmt(parent);

        ASTNode expr = Expr.parse(parent, it);

        returnStmt.addChild(expr);

        return returnStmt;
    }
}

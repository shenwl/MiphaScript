package parser.ast;

import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class ReturnStmt extends Stmt {
    public ReturnStmt() {
        super(ASTNodeTypes.RETURN_STMT, "return");
    }

    public static ASTNode parse(PeekTokenIterator it) throws ParserException {
        it.nextMatch("return");
        ASTNode returnStmt = new ReturnStmt();

        ASTNode expr = Expr.parse(it);

        returnStmt.addChild(expr);

        return returnStmt;
    }
}

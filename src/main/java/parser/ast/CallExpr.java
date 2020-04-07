package parser.ast;

import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class CallExpr extends Expr {
    public CallExpr() {
        super();
        this.label = "call";
        this.type = ASTNodeTypes.CALL_EXPR;
    }

    public static ASTNode parse(ASTNode factor, PeekTokenIterator it) throws ParserException {
        CallExpr callExpr = new CallExpr();
        callExpr.addChild(factor);

        it.nextMatch("(");

        ASTNode p;
        while((p = Expr.parse(it)) != null) {
            callExpr.addChild(p);
            if(!it.peek().getValue().equals(")")) {
                it.nextMatch(",");
            }
        }

        it.nextMatch(")");

        return callExpr;
    }
}

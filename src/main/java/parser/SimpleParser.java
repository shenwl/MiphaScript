package parser;

import parser.ast.ASTNode;
import parser.ast.ASTNodeTypes;
import parser.ast.Expr;
import parser.ast.Scalar;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public class SimpleParser {
    // Expr -> digit + Expr | digit
    // digit: 0|1|2|...|9
    public static ASTNode parse(PeekTokenIterator it) throws ParserException {

        Expr expr = new Expr(null);
        Scalar scalar = new Scalar(expr, it);

        // base condition
        if(!it.hasNext()) {
            return scalar;
        }

        expr.setLexeme(it.peek());
        it.nextMatch("+");
        expr.setLabel("+");
        expr.setType(ASTNodeTypes.BINARY_EXPR);

        expr.addChild(scalar);
        ASTNode rightNode = parse(it);
        expr.addChild(rightNode);

        return expr;
    }
}

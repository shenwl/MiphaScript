package parser.utils;

import parser.ast.ASTNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ParserUtils {
    public static String toPostfixExpression(ASTNode node) {
        // left op right -> left right op
        String leftStr = "";
        String rightStr = "";

        switch (node.getType()) {
            case BINARY_EXPR:
                leftStr = toPostfixExpression(node.getChild(0));
                rightStr = toPostfixExpression(node.getChild(1));
                return leftStr + " " + rightStr + " " + node.getLexeme().getValue();
            case VARIABLE:
            case SCALAR:
                return node.getLexeme().getValue();
        }
        throw new NotImplementedException();
    }
}

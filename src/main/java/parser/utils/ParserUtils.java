package parser.utils;

import parser.ast.ASTNode;

public class ParserUtils {
    public static String toPostfixExpression(ASTNode node) {
        // left op right -> left right op
        String leftStr = "";
        String rightStr = "";

        switch (node.getType()) {
            case BINARY_EXPR:
            case DECLARE_STMT:
            case ASSIGN_STMT:
                leftStr = toPostfixExpression(node.getChild(0));
                rightStr = toPostfixExpression(node.getChild(1));
                return leftStr + " " + rightStr + " " + node.getLexeme().getValue();
            case VARIABLE:
            case SCALAR:
                return node.getLexeme().getValue();
        }
        throw new RuntimeException("toPostfixExpression Exception: not impl");
    }
}

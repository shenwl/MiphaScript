package parser.utils;

import com.sun.deploy.util.StringUtils;
import parser.ast.ASTNode;

import java.util.ArrayList;
import java.util.LinkedList;

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

    public static String toBFSString(ASTNode root, int max) {
        LinkedList<ASTNode> queue = new LinkedList<>();
        ArrayList<String> list = new ArrayList<>();

        queue.add(root);

        int i = 0;
        while(queue.size() > 0 && i++ < max) {
            ASTNode node = queue.poll();
            list.add(node.getLabel());
            queue.addAll(node.getChildren());
        }

        return StringUtils.join(list, " ");
    }
}

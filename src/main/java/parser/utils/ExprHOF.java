package parser.utils;

import parser.ast.ASTNode;

@FunctionalInterface
public interface ExprHOF {
    ASTNode hoc() throws ParserException;
}

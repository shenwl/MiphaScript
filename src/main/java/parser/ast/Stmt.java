package parser.ast;

import lexer.Token;
import parser.utils.ParserException;
import parser.utils.PeekTokenIterator;

public abstract class Stmt extends ASTNode {
    public Stmt(ASTNode parent, ASTNodeTypes type, String label) {
        super(parent, type, label);
    }

    public static ASTNode parseStmt(ASTNode parent, PeekTokenIterator it) throws ParserException {
        // 解析语句需要往前看两个
        Token token = it.next();
        Token lookahead = it.peek();
        it.putBack();

        if(token.isVariable() && lookahead.getValue().equals("=")) {
            return AssignStmt.parse(parent, it);
        }
        if(token.getValue().equals("var")) {
            return DeclareStmt.parse(parent, it);
        }
        // TODO: 其他case
        return null;
    }
}
